const rotulosStatusTransferencia = {
    AGENDADA: "Agendada",
    PROCESSANDO: "Processando",
    CONCLUIDA: "Concluída",
    FALHA: "Falhou",
    CANCELADA: "Cancelada"
};

function formatarDataTransferencia(data) {
    if (!data) return "—";

    return new Intl.DateTimeFormat("pt-BR", {
        dateStyle: "short",
        timeStyle: "short"
    }).format(new Date(data));
}

function valorDataHoraLocal(data) {
    const doisDigitos = valor => String(valor).padStart(2, "0");

    return `${data.getFullYear()}-${doisDigitos(data.getMonth() + 1)}-${doisDigitos(data.getDate())}`
        + `T${doisDigitos(data.getHours())}:${doisDigitos(data.getMinutes())}`;
}

function contasAtivasParaTransferencia() {
    return fourbankSession.contas.filter(conta => conta.status === "ATIVA");
}

function opcoesContasParaTransferencia() {
    return contasAtivasParaTransferencia().map(conta => `
        <option value="${conta.tipo}">
            ${nomeTipoConta(conta.tipo)} · Ag. ${escaparHtml(conta.agencia)} · Conta ${escaparHtml(conta.numero)}
            · ${formatarMoeda(Number(conta.saldo))}
        </option>
    `).join("");
}

function renderizarPaginaTransferencia() {
    const content = document.getElementById("content");
    const contasAtivas = contasAtivasParaTransferencia();

    content.innerHTML = `
        <div class="transfer-page">
            <div class="transfer-heading">
                <div>
                    <button class="transfer-back" type="button" onclick="loadPage('dashboard')">
                        <span class="material-symbols-outlined" aria-hidden="true">arrow_back</span>
                        Voltar
                    </button>
                    <p class="transfer-eyebrow">Área de transferências</p>
                    <h1>Transferir dinheiro</h1>
                    <p>Envie agora ou escolha uma data para agendar.</p>
                </div>
                <div class="transfer-secure-badge">
                    <span class="material-symbols-outlined" aria-hidden="true">lock</span>
                    Ambiente seguro
                </div>
            </div>

            ${contasAtivas.length ? `
                <div class="transfer-layout">
                    <section class="card transfer-form-card">
                        <form id="transfer-form">
                            <fieldset class="transfer-mode" aria-label="Quando transferir">
                                <legend>Quando você quer transferir?</legend>
                                <label>
                                    <input type="radio" name="modoTransferencia" value="IMEDIATA" checked>
                                    <span>
                                        <span class="material-symbols-outlined" aria-hidden="true">bolt</span>
                                        Agora
                                    </span>
                                </label>
                                <label>
                                    <input type="radio" name="modoTransferencia" value="AGENDADA">
                                    <span>
                                        <span class="material-symbols-outlined" aria-hidden="true">calendar_month</span>
                                        Agendar
                                    </span>
                                </label>
                            </fieldset>

                            <div class="transfer-section-title">
                                <span>1</span>
                                <div>
                                    <h2>Conta de origem</h2>
                                    <p>Escolha de onde o valor será debitado.</p>
                                </div>
                            </div>

                            <label class="transfer-field" for="transfer-origin-account">
                                Minha conta
                                <select id="transfer-origin-account" name="tipoContaOrigem" required>
                                    ${opcoesContasParaTransferencia()}
                                </select>
                            </label>
                            <div id="transfer-origin-summary" class="transfer-origin-summary"></div>

                            <div class="transfer-section-title">
                                <span>2</span>
                                <div>
                                    <h2>Dados de quem vai receber</h2>
                                    <p>Os dados precisam ser iguais aos cadastrados no 4Bank.</p>
                                </div>
                            </div>

                            <div class="transfer-fields-grid">
                                <label class="transfer-field transfer-field-wide" for="transfer-recipient-name">
                                    Nome completo
                                    <input id="transfer-recipient-name" name="nomeDestinatario" type="text"
                                        autocomplete="name" placeholder="Ex.: Amanda Oliveira" required>
                                </label>
                                <label class="transfer-field transfer-field-wide" for="transfer-recipient-document">
                                    CPF ou CNPJ
                                    <input id="transfer-recipient-document" name="documentoDestinatario" type="text"
                                        inputmode="numeric" placeholder="Somente números ou com pontuação" required>
                                </label>
                                <label class="transfer-field" for="transfer-destination-agency">
                                    Agência
                                    <input id="transfer-destination-agency" name="agenciaDestino" type="text"
                                        inputmode="numeric" placeholder="0001" required>
                                </label>
                                <label class="transfer-field" for="transfer-destination-account">
                                    Número da conta
                                    <input id="transfer-destination-account" name="numeroContaDestino" type="text"
                                        inputmode="numeric" placeholder="Número da conta" required>
                                </label>
                                <label class="transfer-field transfer-field-wide" for="transfer-destination-type">
                                    Tipo da conta de destino
                                    <select id="transfer-destination-type" name="tipoContaDestino" required>
                                        <option value="CORRENTE">Conta corrente</option>
                                        <option value="POUPANCA">Conta poupança</option>
                                    </select>
                                </label>
                            </div>

                            <div class="transfer-section-title">
                                <span>3</span>
                                <div>
                                    <h2>Valor e data</h2>
                                    <p>Informe o valor e, se agendada, a data e horário da transferência.</p>
                                </div>
                            </div>

                            <div class="transfer-fields-grid">
                                <label class="transfer-field" for="transfer-amount">
                                    Valor
                                    <div class="transfer-money-input">
                                        <span>R$</span>
                                        <input id="transfer-amount" name="valor" type="number" min="0.01" step="0.01"
                                            inputmode="decimal" placeholder="0,00" required>
                                    </div>
                                </label>
                                <label class="transfer-field" id="transfer-schedule-field" for="transfer-scheduled-at" hidden>
                                    Data e horário
                                    <input id="transfer-scheduled-at" name="agendadaPara" type="datetime-local">
                                </label>
                            </div>

                            <p id="transfer-feedback" class="transfer-feedback" aria-live="polite" hidden></p>

                            <div class="transfer-submit-row">
                                <p>
                                    <span class="material-symbols-outlined" aria-hidden="true">info</span>
                                    Confira os dados antes de continuar.
                                </p>
                                <button class="btn transfer-submit" type="submit">Fazer transferência</button>
                            </div>
                        </form>
                    </section>

                    <aside class="transfer-side-column">
                        <section id="transfer-receipt" class="card transfer-receipt" hidden></section>
                        <section class="card transfer-history-card">
                            <div class="transfer-history-heading">
                                <div>
                                    <p class="transfer-eyebrow">Movimentações</p>
                                    <h2>Transferências da conta</h2>
                                </div>
                                <button class="transfer-refresh" id="transfer-refresh-history" type="button"
                                    aria-label="Atualizar transferências">
                                    <span class="material-symbols-outlined" aria-hidden="true">refresh</span>
                                </button>
                            </div>
                            <div id="transfer-history" class="transfer-history" aria-live="polite">
                                <p class="transfer-history-empty">Carregando transferências...</p>
                            </div>
                        </section>
                    </aside>
                </div>
            ` : `
                <section class="card transfer-empty-state">
                    <span class="material-symbols-outlined" aria-hidden="true">account_balance_wallet</span>
                    <h2>Você não tem uma conta ativa</h2>
                    <p>Para transferir, desbloqueie uma conta ou abra uma nova conta disponível.</p>
                    <button class="btn" type="button" onclick="loadPage('dashboard')">Ir para minhas contas</button>
                </section>
            `}
        </div>
    `;

    if (!contasAtivas.length) return;

    configurarFormularioTransferencia();
    carregarHistoricoTransferencias();
}

function configurarFormularioTransferencia() {
    const formulario = document.getElementById("transfer-form");
    const contaOrigem = document.getElementById("transfer-origin-account");
    const dataAgendamento = document.getElementById("transfer-scheduled-at");
    const dataMinima = new Date(Date.now() + 5 * 60 * 1000);

    dataAgendamento.min = valorDataHoraLocal(dataMinima);

    formulario.addEventListener("change", evento => {
        if (evento.target.name === "modoTransferencia") {
            atualizarModoTransferencia(evento.target.value);
        }
    });
    formulario.addEventListener("submit", enviarTransferencia);
    contaOrigem.addEventListener("change", () => {
        atualizarResumoContaOrigem();
        carregarHistoricoTransferencias();
    });
    document.getElementById("transfer-refresh-history")
        .addEventListener("click", carregarHistoricoTransferencias);

    atualizarResumoContaOrigem();
}

function atualizarModoTransferencia(modo) {
    const campoAgendamento = document.getElementById("transfer-schedule-field");
    const dataAgendamento = document.getElementById("transfer-scheduled-at");
    const botao = document.querySelector(".transfer-submit");
    const agendada = modo === "AGENDADA";

    campoAgendamento.hidden = !agendada;
    dataAgendamento.required = agendada;
    if (!agendada) dataAgendamento.value = "";
    botao.textContent = agendada ? "Agendar transferência" : "Fazer transferência";
}

function contaOrigemSelecionada() {
    const tipo = document.getElementById("transfer-origin-account")?.value;
    return contasAtivasParaTransferencia().find(conta => conta.tipo === tipo);
}

function atualizarResumoContaOrigem() {
    const conta = contaOrigemSelecionada();
    const resumo = document.getElementById("transfer-origin-summary");
    if (!conta || !resumo) return;

    resumo.innerHTML = `
        <div>
            <small>Saldo disponível</small>
            <strong>${formatarMoeda(Number(conta.saldo))}</strong>
        </div>
        <span>Ag. ${escaparHtml(conta.agencia)} · Conta ${escaparHtml(conta.numero)}</span>
    `;
}

function atualizarOpcoesContaOrigem() {
    const select = document.getElementById("transfer-origin-account");
    if (!select) return;

    const tipoSelecionado = select.value;
    select.innerHTML = opcoesContasParaTransferencia();
    if (contasAtivasParaTransferencia().some(conta => conta.tipo === tipoSelecionado)) {
        select.value = tipoSelecionado;
    }
}

async function enviarTransferencia(evento) {
    evento.preventDefault();

    const formulario = evento.currentTarget;
    const dados = new FormData(formulario);
    const modo = dados.get("modoTransferencia");
    const botao = formulario.querySelector('button[type="submit"]');
    const feedback = document.getElementById("transfer-feedback");
    const valor = Number(dados.get("valor"));

    if (!Number.isFinite(valor) || valor <= 0) {
        mostrarFeedbackTransferencia("Informe um valor maior que zero.", true);
        return;
    }

    const payload = {
        tipoContaOrigem: dados.get("tipoContaOrigem"),
        nomeDestinatario: dados.get("nomeDestinatario").trim(),
        documentoDestinatario: dados.get("documentoDestinatario").trim(),
        agenciaDestino: dados.get("agenciaDestino").trim(),
        numeroContaDestino: dados.get("numeroContaDestino").trim(),
        tipoContaDestino: dados.get("tipoContaDestino"),
        valor
    };

    if (modo === "AGENDADA") {
        const dataInformada = dados.get("agendadaPara");
        const data = new Date(dataInformada);

        if (!dataInformada || Number.isNaN(data.getTime()) || data <= new Date()) {
            mostrarFeedbackTransferencia("Escolha uma data e um horário futuros.", true);
            return;
        }
        payload.agendadaPara = data.toISOString();
    }

    botao.disabled = true;
    botao.textContent = modo === "AGENDADA" ? "Agendando..." : "Transferindo...";
    feedback.hidden = true;

    try {
        const resultado = await fourbankApi(
            modo === "AGENDADA" ? "/transferencias/agendar" : "/transferencias/realizar",
            {
                method: "POST",
                body: JSON.stringify(payload)
            }
        );

        mostrarComprovanteTransferencia(resultado, modo);
        mostrarFeedbackTransferencia(
            modo === "AGENDADA"
                ? "Transferência agendada com sucesso."
                : "Transferência realizada com sucesso."
        );
        formulario.reset();
        atualizarModoTransferencia("IMEDIATA");

        try {
            fourbankSession.contas = await fourbankApi("/contas");
            saldo = fourbankSession.contas.reduce((total, conta) => total + Number(conta.saldo), 0);
            atualizarOpcoesContaOrigem();
            atualizarResumoContaOrigem();
            await carregarHistoricoTransferencias();
        } catch (erroAtualizacao) {
            mostrarFeedbackTransferencia(
                `A transferência foi aceita, mas os saldos não puderam ser atualizados: ${erroAtualizacao.message}`,
                true
            );
        }
    } catch (erro) {
        mostrarFeedbackTransferencia(erro.message, true);
    } finally {
        botao.disabled = false;
        const modoAtual = formulario.elements.modoTransferencia.value;
        botao.textContent = modoAtual === "AGENDADA"
            ? "Agendar transferência"
            : "Fazer transferência";
    }
}

function mostrarFeedbackTransferencia(mensagem, erro = false) {
    const feedback = document.getElementById("transfer-feedback");
    if (!feedback) return;

    feedback.textContent = mensagem;
    feedback.classList.toggle("error", erro);
    feedback.hidden = false;
}

function mostrarComprovanteTransferencia(transferencia, modo) {
    const comprovante = document.getElementById("transfer-receipt");
    const dataPrincipal = modo === "AGENDADA"
        ? transferencia.agendadaPara
        : transferencia.realizadaEm || transferencia.solicitadaEm;

    comprovante.hidden = false;
    comprovante.innerHTML = `
        <div class="transfer-receipt-icon">
            <span class="material-symbols-outlined" aria-hidden="true">check</span>
        </div>
        <p class="transfer-eyebrow">Tudo certo</p>
        <h2>${modo === "AGENDADA" ? "Transferência agendada" : "Transferência concluída"}</h2>
        <strong>${formatarMoeda(Number(transferencia.valor))}</strong>
        <dl>
            <div><dt>Código</dt><dd>#${escaparHtml(transferencia.id)}</dd></div>
            ${transferencia.nomeDestinatario
                ? `<div><dt>Destinatário</dt><dd>${escaparHtml(transferencia.nomeDestinatario)}</dd></div>`
                : ""}
            <div><dt>Data</dt><dd>${formatarDataTransferencia(dataPrincipal)}</dd></div>
            <div><dt>Taxa</dt><dd>${formatarMoeda(Number(transferencia.taxa || 0))}</dd></div>
            <div><dt>Status</dt><dd>${escaparHtml(rotulosStatusTransferencia[transferencia.status] || transferencia.status)}</dd></div>
        </dl>
    `;
    comprovante.scrollIntoView({ behavior: "smooth", block: "nearest" });
}

async function carregarHistoricoTransferencias() {
    const historico = document.getElementById("transfer-history");
    const conta = contaOrigemSelecionada();
    if (!historico || !conta) return;

    historico.innerHTML = '<p class="transfer-history-empty">Carregando transferências...</p>';

    try {
        const transferencias = await fourbankApi(
            `/transferencias/listar-transferencias/${encodeURIComponent(conta.tipo)}`
        );
        renderizarHistoricoTransferencias(transferencias, conta);
    } catch (erro) {
        historico.innerHTML = `
            <div class="transfer-history-error">
                <span class="material-symbols-outlined" aria-hidden="true">error</span>
                <p>${escaparHtml(erro.message)}</p>
            </div>
        `;
    }
}

function renderizarHistoricoTransferencias(transferencias, conta) {
    const historico = document.getElementById("transfer-history");
    if (!transferencias.length) {
        historico.innerHTML = `
            <div class="transfer-history-empty">
                <span class="material-symbols-outlined" aria-hidden="true">receipt_long</span>
                <p>Nenhuma transferência nesta conta.</p>
            </div>
        `;
        return;
    }

    historico.innerHTML = transferencias.map(transferencia => {
        const enviada = Number(transferencia.contaOrigemId) === Number(conta.id);
        const data = transferencia.realizadaEm
            || transferencia.agendadaPara
            || transferencia.solicitadaEm;
        const status = rotulosStatusTransferencia[transferencia.status] || transferencia.status;

        return `
            <article class="transfer-history-item">
                <div class="transfer-history-icon ${enviada ? "sent" : "received"}">
                    <span class="material-symbols-outlined" aria-hidden="true">
                        ${enviada ? "north_east" : "south_west"}
                    </span>
                </div>
                <div class="transfer-history-info">
                    <strong>${enviada
                        ? `Transferência para ${escaparHtml(transferencia.nomeDestinatario || "outro cliente")}`
                        : `Transferência de ${escaparHtml(transferencia.nomeRemetente || "outro cliente")}`}</strong>
                    <small>${formatarDataTransferencia(data)} · #${escaparHtml(transferencia.id)}</small>
                    <span class="transfer-status ${String(transferencia.status).toLowerCase()}">${escaparHtml(status)}</span>
                </div>
                <strong class="transfer-history-value ${enviada ? "sent" : "received"}">
                    ${enviada ? "−" : "+"} ${formatarMoeda(Number(transferencia.valor))}
                </strong>
            </article>
        `;
    }).join("");
}

const carregarPaginaSemTransferencia = loadPage;

loadPage = function (pagina) {
    carregarPaginaSemTransferencia(pagina);
    if (pagina === "transfer") renderizarPaginaTransferencia();
};
