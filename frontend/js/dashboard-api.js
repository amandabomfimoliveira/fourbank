const FOURBANK_API_URL = "http://localhost:8080/api";
const FOURBANK_TOKEN_KEY = "fourbank.token";
const fourbankSession = {
  token: sessionStorage.getItem(FOURBANK_TOKEN_KEY),
  usuario: null,
  contas: []
};
let encerramentoPendente = null;
let mostrarContasEncerradas = false;

function escaparHtml(valor) {
  return String(valor ?? "").replace(/[&<>'"]/g, caractere => ({
    "&": "&amp;",
    "<": "&lt;",
    ">": "&gt;",
    "'": "&#39;",
    '"': "&quot;"
  })[caractere]);
}

async function fourbankApi(caminho, opcoes = {}) {
  const headers = {
    Accept: "application/json",
    Authorization: `Bearer ${fourbankSession.token}`,
    ...opcoes.headers
  };
  if (opcoes.body) headers["Content-Type"] = "application/json";

  let resposta;
  try {
    resposta = await fetch(`${FOURBANK_API_URL}${caminho}`, {
      ...opcoes,
      headers
    });
  } catch {
    throw new Error("Não foi possível conectar ao backend na porta 8080.");
  }

  const corpo = await resposta.json().catch(() => null);
  if (!resposta.ok) {
    if (resposta.status === 401) {
      sessionStorage.removeItem(FOURBANK_TOKEN_KEY);
      window.location.replace("index.html");
    }
    throw new Error(corpo?.mensagem || "Não foi possível carregar os dados da sua conta.");
  }
  return corpo;
}

function nomeTipoConta(tipo) {
  return tipo === "POUPANCA" ? "Conta poupança" : "Conta corrente";
}

function tiposContaDisponiveis() {
  const tiposContratados = new Set(
    fourbankSession.contas
      .filter(conta => conta.status !== "ENCERRADA")
      .map(conta => conta.tipo)
  );
  return ["CORRENTE", "POUPANCA"].filter(tipo => !tiposContratados.has(tipo));
}

function renderizarAcoesConta(conta) {
  if (conta.status === "ATIVA") {
    return `
      <div class="backend-account-card-actions">
        <button class="backend-account-action" type="button" data-account-action="bloquear" data-account-type="${conta.tipo}">Bloquear</button>
        <button class="backend-account-action danger" type="button" data-account-action="encerrar" data-account-type="${conta.tipo}">Encerrar</button>
      </div>
    `;
  }

  if (conta.status === "BLOQUEADA") {
    return `
      <div class="backend-account-card-actions">
        <button class="backend-account-action" type="button" data-account-action="desbloquear" data-account-type="${conta.tipo}">Desbloquear</button>
      </div>
    `;
  }

  return '<div class="backend-account-card-actions"><span>Conta encerrada</span></div>';
}

function renderizarContasConectadas() {
  const cabecalho = document.querySelector("#content .header");
  if (!cabecalho) return;
  document.querySelector(".backend-accounts")?.remove();

  const tiposDisponiveis = tiposContaDisponiveis();
  const quantidadeEncerradas = fourbankSession.contas
    .filter(conta => conta.status === "ENCERRADA")
    .length;
  const contasVisiveis = mostrarContasEncerradas
    ? fourbankSession.contas
    : fourbankSession.contas.filter(conta => conta.status !== "ENCERRADA");

  const contas = contasVisiveis.map(conta => `
    <article class="backend-account-card">
      <div>
        <small>${nomeTipoConta(conta.tipo)}</small>
        <strong>${formatarMoeda(Number(conta.saldo))}</strong>
      </div>
      <span class="backend-status ${conta.status.toLowerCase()}">${escaparHtml(conta.status)}</span>
      <p>Agência ${escaparHtml(conta.agencia)} · Conta ${escaparHtml(conta.numero)}</p>
      ${renderizarAcoesConta(conta)}
    </article>
  `).join("");

  cabecalho.insertAdjacentHTML("afterend", `
    <section class="backend-accounts" aria-label="Contas bancárias">
      <div class="backend-accounts-title">
        <div><h3>Minhas contas</h3></div>
        <div class="backend-accounts-actions">
          <span>${contasVisiveis.length} ${contasVisiveis.length === 1 ? "conta visível" : "contas visíveis"}</span>
          ${quantidadeEncerradas ? `
            <button class="backend-closed-accounts-toggle" id="toggle-closed-accounts" type="button"
              aria-pressed="${mostrarContasEncerradas}">
              <span class="material-symbols-outlined" aria-hidden="true">
                ${mostrarContasEncerradas ? "visibility_off" : "visibility"}
              </span>
              ${mostrarContasEncerradas ? "Ocultar encerradas" : `Mostrar encerradas (${quantidadeEncerradas})`}
            </button>
          ` : ""}
          ${tiposDisponiveis.length
            ? '<button class="btn" id="open-account-modal" type="button">+ Nova conta</button>'
            : '<span class="backend-all-accounts"></span>'}
        </div>
      </div>
      <div class="backend-accounts-grid">${contas || "<p>Nenhuma conta ativa ou bloqueada para exibir.</p>"}</div>
    </section>
  `);

  document.getElementById("open-account-modal")?.addEventListener("click", abrirModalNovaConta);
  document.getElementById("toggle-closed-accounts")?.addEventListener("click", () => {
    mostrarContasEncerradas = !mostrarContasEncerradas;
    renderizarContasConectadas();
  });
  document.querySelector(".backend-accounts")?.addEventListener("click", evento => {
    const botao = evento.target.closest("[data-account-action]");
    if (botao) executarAcaoConta(botao.dataset.accountAction, botao.dataset.accountType, botao);
  });
}

function mostrarMensagemContas(mensagem, erro = false) {
  const secao = document.querySelector(".backend-accounts");
  if (!secao) return;
  secao.querySelector(".backend-account-feedback")?.remove();
  secao.insertAdjacentHTML(
    "afterbegin",
    `<p class="backend-account-feedback${erro ? " error" : ""}" role="${erro ? "alert" : "status"}">${escaparHtml(mensagem)}</p>`
  );
}

function executarAcaoConta(acao, tipo, botao) {
  if (acao === "encerrar") {
    abrirModalEncerramento(tipo);
    return;
  }
  executarAcaoContaConfirmada(acao, tipo, botao);
}

function abrirModalEncerramento(tipo) {
  encerramentoPendente = tipo;
  document.getElementById("end-account-description").textContent =
    `Tem certeza que deseja encerrar sua ${nomeTipoConta(tipo).toLowerCase()}?`;
  document.getElementById("end-account-modal").hidden = false;
  document.body.classList.add("backend-modal-open");
  document.getElementById("confirm-end-account").focus();
}

function fecharModalEncerramento() {
  encerramentoPendente = null;
  document.getElementById("end-account-modal").hidden = true;
  document.body.classList.remove("backend-modal-open");
}

async function confirmarEncerramento() {
  if (!encerramentoPendente) return;
  const tipo = encerramentoPendente;
  const botao = document.getElementById("confirm-end-account");
  fecharModalEncerramento();
  await executarAcaoContaConfirmada("encerrar", tipo, botao);
}

async function executarAcaoContaConfirmada(acao, tipo, botao) {
  const nomesAcao = {
    bloquear: "Bloqueando...",
    desbloquear: "Desbloqueando...",
    encerrar: "Encerrando..."
  };

  const textoOriginal = botao.textContent;
  botao.disabled = true;
  botao.textContent = nomesAcao[acao];
  document.querySelector(".backend-account-feedback")?.remove();

  try {
    const caminho = acao === "encerrar" ? "/contas" : `/contas/${acao}`;
    await fourbankApi(caminho, {
      method: acao === "encerrar" ? "DELETE" : "PATCH",
      body: JSON.stringify({ tipo })
    });
    fourbankSession.contas = await fourbankApi("/contas");
    saldo = fourbankSession.contas.reduce((total, conta) => total + Number(conta.saldo), 0);
    renderizarContasConectadas();
    const mensagens = {
      bloquear: "Conta bloqueada com sucesso.",
      desbloquear: "Conta desbloqueada com sucesso.",
      encerrar: "Conta encerrada com sucesso."
    };
    mostrarMensagemContas(mensagens[acao]);
  } catch (erro) {
    mostrarMensagemContas(erro.message, true);
  } finally {
    botao.disabled = false;
    botao.textContent = textoOriginal;
  }
}

function abrirModalNovaConta() {
  const tiposDisponiveis = tiposContaDisponiveis();
  if (!tiposDisponiveis.length) return;

  const modal = document.getElementById("new-account-modal");
  const select = document.getElementById("new-account-type");
  const mensagem = document.getElementById("new-account-message");
  select.innerHTML = tiposDisponiveis
    .map(tipo => `<option value="${tipo}">${nomeTipoConta(tipo)}</option>`)
    .join("");
  mensagem.textContent = "";
  mensagem.hidden = true;
  modal.hidden = false;
  document.body.classList.add("backend-modal-open");
  select.focus();
}

function fecharModalNovaConta() {
  document.getElementById("new-account-modal").hidden = true;
  document.body.classList.remove("backend-modal-open");
}

async function criarNovaConta(evento) {
  evento.preventDefault();
  const formulario = evento.currentTarget;
  const botao = formulario.querySelector('button[type="submit"]');
  const tipo = document.getElementById("new-account-type").value;
  const mensagem = document.getElementById("new-account-message");
  const textoOriginal = botao.textContent;

  botao.disabled = true;
  botao.textContent = "Abrindo...";
  mensagem.hidden = true;

  try {
    await fourbankApi("/contas", {
      method: "POST",
      body: JSON.stringify({ tipo })
    });
    fourbankSession.contas = await fourbankApi("/contas");
    saldo = fourbankSession.contas.reduce((total, conta) => total + Number(conta.saldo), 0);
    fecharModalNovaConta();
    renderizarContasConectadas();
    mostrarMensagemContas("Sua nova conta foi aberta com sucesso.");
  } catch (erro) {
    mensagem.textContent = erro.message;
    mensagem.hidden = false;
  } finally {
    botao.disabled = false;
    botao.textContent = textoOriginal;
  }
}

document.getElementById("new-account-form").addEventListener("submit", criarNovaConta);
document.getElementById("close-account-modal").addEventListener("click", fecharModalNovaConta);
document.getElementById("cancel-account-modal").addEventListener("click", fecharModalNovaConta);
document.getElementById("new-account-modal").addEventListener("click", evento => {
  if (evento.target.id === "new-account-modal") fecharModalNovaConta();
});
document.getElementById("confirm-end-account").addEventListener("click", confirmarEncerramento);
document.getElementById("dismiss-end-account-modal").addEventListener("click", fecharModalEncerramento);
document.getElementById("cancel-end-account-modal").addEventListener("click", fecharModalEncerramento);
document.getElementById("end-account-modal").addEventListener("click", evento => {
  if (evento.target.id === "end-account-modal") fecharModalEncerramento();
});
document.addEventListener("keydown", evento => {
  if (evento.key === "Escape" && !document.getElementById("new-account-modal").hidden) {
    fecharModalNovaConta();
  }
  if (evento.key === "Escape" && !document.getElementById("end-account-modal").hidden) {
    fecharModalEncerramento();
  }
});

const carregarPaginaOriginal = loadPage;
loadPage = function (pagina) {
  carregarPaginaOriginal(pagina);
  document.querySelectorAll(".sidebar button").forEach(botao => botao.classList.remove("menu-active"));
  const paginasMenu = ["dashboard", "pix", "transfer", "invest", "cartao", "extrato"];
  const indice = paginasMenu.indexOf(pagina);
  if (indice >= 0) document.querySelectorAll(".sidebar button")[indice]?.classList.add("menu-active");
  if (pagina === "dashboard" && fourbankSession.usuario) renderizarContasConectadas();
};

function sairDaConta() {
  sessionStorage.removeItem(FOURBANK_TOKEN_KEY);
  window.location.replace("index.html");
}

async function carregarDadosDoBackend() {
  if (!fourbankSession.token) {
    window.location.replace("index.html");
    return;
  }

  try {
    const [usuario, contas] = await Promise.all([
      fourbankApi("/users/me"),
      fourbankApi("/contas")
    ]);
    fourbankSession.usuario = usuario;
    fourbankSession.contas = contas;
    saldo = contas.reduce((total, conta) => total + Number(conta.saldo), 0);

    const primeiroNome = usuario.nome.trim().split(/\s+/)[0];
    document.getElementById("dashboard-user").innerHTML = `
      <span>Olá, <strong>${escaparHtml(primeiroNome)}</strong></span>
      <button class="logout-dashboard" type="button" onclick="sairDaConta()">Sair</button>
    `;
    loadPage("dashboard");
  } catch (erro) {
    document.getElementById("dashboard-user").textContent = "Falha ao carregar";
    document.getElementById("content").innerHTML = `
      <div class="card backend-error">
        <h2>Não foi possível abrir seu dashboard</h2>
        <p>${escaparHtml(erro.message)}</p>
        <button class="btn" type="button" onclick="carregarDadosDoBackend()">Tentar novamente</button>
        <button class="btn-outline" type="button" onclick="sairDaConta()">Voltar ao início</button>
      </div>
    `;
  }
}

carregarDadosDoBackend();
