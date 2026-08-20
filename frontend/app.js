const investimentosUsuario = [
  {
    id: 1,
    titulo: "CDB PORQUINHO OBJETIVO",
    vencimento: "22/06/2026",
    valor: 1250.50
  }
  
];

let saldo = 5000.00;

let filtroAtual = "todos";
let chavePixUsuario = null;

let extrato = [
  {
    tipo: "Saldo inicial",
    valor: 5200,
    data: new Date().toLocaleString()
  }
];


/*Cartão */

let cartao = {
  limite: 2000,
  usado: 0,
  fatura: 0,
  vencimento: "10/07/2026",
  numero: "**** **** **** 1234"
};

let comprasCartao = [];

function loadPage(page){

  let content = document.getElementById('content');

 if(page === 'dashboard'){

  content.innerHTML = `

  <!-- HEADER -->
  <div class="header">
    <h2>Dashboard</h2>
  </div>

  <!-- RESUMO -->
  <div class="grid-3">

    <div class="card highlight">
      <p>Saldo total</p>
      <h2>${formatarMoeda(saldo)}</h2>
    </div>

    
    <div class="card">
      <p>Gastos cartão</p>
      <h2>${formatarMoeda(cartao.fatura)}</h2>
    </div>

    <div class="card">
      <p>Investimentos</p>
      <h2>${formatarMoeda(
        investimentosUsuario.reduce((total,item) => total + calcularResgate(item),0)
      )}</h2>
    </div>

  </div>
 <!-- ✅ NOVO MENU (POSIÇÃO CORRETA) -->
  <div class="menu-grid">

    <div class="menu-item" onclick="loadPage('pix')">
      
    <div class="icon">
        <span class="material-symbols-outlined" id="pix">
window
</span>
    </div>
    <span>Pix</span>

    </div>

    <div class="menu-item" onclick="loadPage('transfer')">      
        <div class="icon">
        <span class="material-symbols-outlined">sync_alt</span>
        </div>
        <span>Transferência</span>
    </div>

    <div class="menu-item" onclick="loadPage('invest')">
      
<div class="icon">
      <span class="material-symbols-outlined">monitoring</span>
    </div>
    <span>Renda Fixa</span>

    </div>

    <div class="menu-item" onclick="loadPage('cartao')">
      
<div class="icon">
      <span class="material-symbols-outlined">credit_card</span>
    </div>
    <span>Cartões</span>

    </div>

  </div>
  <!-- GRÁFICO -->
  
  <div class="card">
    <h3>Resumo Financeiro</h3>
    <div id="graficoDash"></div>
  </div>
  <!-- Limite Cartão -->
 <div class="limite-cartao">

    <h4>Limite disponível</h4>

    <strong>
        ${formatarMoeda(
            cartao.limite - cartao.usado
        )}
    </strong>

</div>
 


  <!-- CARTÃO VISUAL -->
  <div class="card card-visual">
    <p>${cartao.numero}</p>
    <strong>Limite: ${formatarMoeda(cartao.limite)}</strong>
    <small>Disponível: ${formatarMoeda(cartao.limite - cartao.usado)}</small>
  </div>

  <!-- TRANSAÇÕES -->
  <div class="card">
    <div class="row">
      <h3>Transações recentes</h3>
      <button onclick="loadPage('extrato')">Ver mais</button>
    </div>

    ${extrato.slice().reverse().slice(0,5).map(item=>`
      <div class="row">
        <span>${item.tipo}</span>
        <strong style="color:${item.valor<0?'red':'green'}">
          ${formatarMoeda(item.valor)}
        </strong>
      </div>
    `).join("")}

  </div>

  `;

  renderGraficoDashboard();
}


  if(page === 'pix'){
  content.innerHTML = `

  <div class="pix-container">

    <!-- T1 -->
    <div id="t1">
      <div class="pix-header">Pix</div>

      <div class="item" onclick="goT2('Novo contato')">➕ Novo contato</div>
      <div class="item" onclick="goT2('Alexandre Souza')">Alexandre Souza</div>
      <div class="item" onclick="goT2('Ana Lima')">Ana Lima</div>
    </div>

    <!-- T2 -->
    <div id="t2" class="hidden">
      <div class="pix-header">
        <span class="back" onclick="back(1)">←</span> Tipo de chave
      </div>

      <div class="item" onclick="setKey('CPF')">CPF</div>
      <div class="item" onclick="setKey('Email')">Email</div>
      <div class="item" onclick="setKey('Telefone')">Telefone</div>
      <div class="item" onclick="setKey('Aleatória')">Chave aleatória</div>
    </div>

    <!-- T3 -->
    <div id="t3" class="hidden">
      <div class="pix-header">
        <span class="back" onclick="back(2)">←</span> Dados
      </div>

      <input id="chave" class="input" placeholder="Chave">
      <input id="valor" type="number" class="input" placeholder="Valor">
      <input id="desc" class="input" placeholder="Descrição">

      <button class="btn" onclick="goT4()">Continuar</button>
    </div>

    <!-- T4 -->
    <div id="t4" class="hidden">
      <div class="pix-header">
        <span class="back" onclick="back(3)">←</span> Confirmar
      </div>

      <div class="summary">
        <div class="row"><span>Contato</span><span id="sContato"></span></div>
        <div class="row"><span>Tipo</span><span id="sTipo"></span></div>
        <div class="row"><span>Chave</span><span id="sChave"></span></div>
        <div class="row"><span>Valor</span><span id="sValor"></span></div>
      </div>

      <button class="btn" onclick="executarPix()">Transferir</button>
    </div>

    <!-- LOADING -->
    <div id="loading" class="hidden loading">
      <p>🔄 Processando Pix...</p>
    </div>

    <!-- T5 -->
    <div id="t5" class="hidden">
      <div class="center">
        <div class="success">✅</div>
        <h2>Pagamento realizado</h2>
      </div>

      <div class="receipt">
        <div class="row"><span>ID:</span><span id="rId"></span></div>
        <div class="row"><span>Data:</span><span id="rData"></span></div>
        <div class="row"><span>Contato:</span><span id="rContato"></span></div>
        <div class="row"><span>Valor:</span><span id="rValor"></span></div>
      </div>

      <button class="btn" onclick="resetarPix()">Novo Pix</button>
      <button class="btn-outline" onclick="loadPage('dashboard')">Voltar</button>
    </div>

  </div>
  `;
}

  if(page === 'invest'){
  content.innerHTML = `

    <div class="tabs">
      <button class="tab active" onclick="loadPage('invest')">
        Produtos de Renda Fixa
      </button>

      <button class="tab" onclick="loadPage('resgate')">
        Resgatar meus Produtos de Renda Fixa
      </button>
    </div>

    <div class="card filters">
      <input placeholder="🔎 Nome do Produto">
      <button class="btn-outline">Filtros</button>
    </div>

    ${createProductCard("LCI LIQUIDEZ 6 MESES", "91% do CDI", "R$ 50", "22/06/2031", "Após 185 dias")}
    ${createProductCard("LCI PRÉ 180 DIAS", "12,8% a.a.", "R$ 50", "22/12/2026", "No vencimento")}

  `;
}

  if(page === 'extrato'){

  content.innerHTML = `

  <div class="card highlight">
    <h2>Extrato</h2>
  </div>

  <!-- FILTROS -->
  <div class="card">
    <button id="btnTodos" class="filtro-btn ativo" onclick="filtrarExtrato('todos', this)">
  Todos
</button>

<button id="btnPix" class="filtro-btn" onclick="filtrarExtrato('pix', this)">
  Pix
</button>

<button id="btnCartao" class="filtro-btn" onclick="filtrarExtrato('cartao', this)">
  Cartão
</button>

<button id="btnInvest" class="filtro-btn" onclick="filtrarExtrato('invest', this)">
  Invest
</button>
  </div>

  <!-- GRÁFICO -->
  <div class="card">
    <h3>Resumo Financeiro</h3>
    <div id="graficoExtrato"></div>
    <div id="resumoValores" class="resumo-valores"></div>
  </div>

  <!-- LISTA -->
  <div id="listaExtrato"></div>

  `;

  renderExtrato();
  renderGraficoExtrato();
  atualizarFiltroAtivo();
}

  if(page === 'cartao'){

  content.innerHTML = `

  <!-- CARD -->
  <div class="card highlight">
    <h2>Cartão</h2>
    <p>${cartao.numero}</p>

    <p>Limite: ${formatarMoeda(cartao.limite)}</p>
    <p>Disponível: ${formatarMoeda(cartao.limite - cartao.usado)}</p>
  </div>

  <!-- COMPRA -->
  <div class="card">
    <h3>Simular compra</h3>

    <input id="compraNome" class="input" placeholder="Ex: iFood">
    <input id="compraValor" type="number" class="input" placeholder="Valor">

    <button class="btn" onclick="comprarCartao(
      document.getElementById('compraNome').value,
      document.getElementById('compraValor').value
    )">
      Comprar
    </button>
  </div>

  <!-- FATURA -->
  <div class="card">
    <h3>Fatura atual</h3>
    <h2>${formatarMoeda(cartao.fatura)}</h2>
    <small>Vence em ${cartao.vencimento}</small>

    <button class="btn" onclick="loadPage('fatura_cartao')">
      Ver fatura
    </button>
  </div>

  `;

}


  if(page === 'fatura_cartao'){

  let lista = comprasCartao.map(c => `
    <div class="row">
      <span>${c.nome}</span>
      <span>- ${formatarMoeda(c.valor)}</span>
    </div>
  `).join("");

  content.innerHTML = `

    <h2>Fatura do Cartão</h2>

    <div class="card">
      <p>Total: ${formatarMoeda(cartao.fatura)}</p>
    </div>

    <div class="card">
      ${lista || "<p>Nenhuma compra</p>"}
    </div>

    <button class="btn" onclick="pagarFatura()">
      Pagar Fatura
    </button>

    <button class="btn-outline" onclick="loadPage('cartao')">
      Voltar
    </button>

  `;
}

  if(page === 'resgate'){

  let html = `

    <div class="tabs">
      <button class="tab" onclick="loadPage('invest')">
        Produtos de Renda Fixa
      </button>

      <button class="tab active">
        Resgatar meus Produtos de Renda Fixa
      </button>
    </div>

    <div class="card">

      <div class="resgate-header">
        <strong>Título</strong>
        <strong>Vencimento</strong>
        <strong>Valor</strong>
        <strong></strong>
      </div>
  `;

  investimentosUsuario.forEach(item => {

    html += `
      <div class="resgate-item">

        <div>${item.titulo}</div>
        <div>${item.vencimento}</div>
        
<div>
${formatarMoeda(
    calcularResgate(item)
)}
</div>


        <div>
          <button
  class="btn-outline"
  onclick="resgatar(${item.id})">
  RESGATAR
</button>
        </div>

      </div>
    `;
  });

  html += `</div>`;

  content.innerHTML = html;
}
}


/* ✅ FORA DO LOADPAGE (IMPORTANTE) */

let pixState = {};

// NAV
function show(id) {
  document.querySelectorAll(".pix-container > div")
    .forEach(d => d.classList.add("hidden"));

  document.getElementById(id).classList.remove("hidden");
}

// T1 → T2
function goT2(nome) {
  pixState.contato = nome;
  show("t2");
}

// SELEÇÃO CHAVE
function setKey(tipo) {
  pixState.tipo = tipo;
  show("t3");
}

// T3 → T4
function goT4() {
  pixState.chave = document.getElementById("chave").value;
  pixState.valor = document.getElementById("valor").value;

  document.getElementById("sContato").innerText = pixState.contato;
  document.getElementById("sTipo").innerText = pixState.tipo;
  document.getElementById("sChave").innerText = pixState.chave;
  document.getElementById("sValor").innerText = "R$ " + pixState.valor;

  show("t4");
}
// Grafico
function renderGraficoDashboard(){

    const container = document.getElementById("graficoDash");

    if(!container) return;

    container.innerHTML = "";

    let entradas = [];
    let saidas = [];

    extrato.forEach(item => {

        if(item.valor >= 0){
            entradas.push(item.valor);
        }else{
            saidas.push(Math.abs(item.valor));
        }

    });

    while(entradas.length < 9){
        entradas.push(0);
    }

    while(saidas.length < 9){
        saidas.push(0);
    }

    const options = {

        chart: {
            type: 'area',
            height: 350,
            toolbar: {
                show: false
            },
            background: 'transparent'
        },

        series: [
            {
                name: 'Entradas',
                data: entradas.slice(-9)
            },
            {
                name: 'Saídas',
                data: saidas.slice(-9)
            }
        ],

        colors: [
            '#3b82f6',
            '#f97316'
        ],

        stroke: {
            curve: 'smooth',
            width: 3
        },

        fill: {
            opacity: [0.15,0.05]
        },

        markers: {
            size: 6,
            strokeWidth: 2
        },

        xaxis: {
            categories: [
                '01','02','03','04','05',
                '06','07','08','09'
            ],
            labels:{
                style:{
                    colors:'#94a3b8'
                }
            }
        },

        yaxis:{
            labels:{
                style:{
                    colors:'#94a3b8'
                }
            }
        },

        grid:{
            borderColor:'rgba(255,255,255,.08)'
        },

        legend:{
            labels:{
                colors:'#ffffff'
            },
              show:false
        },

        tooltip:{
            theme:'dark'
        }
    };

    new ApexCharts(
        container,
        options
    ).render();
}

function filtrarExtrato(tipo, botao){

    filtroAtual = tipo;

    document
        .querySelectorAll(".filtro-btn")
        .forEach(btn =>
            btn.classList.remove("ativo")
        );

    botao.classList.add("ativo");

    renderExtrato();

    renderGraficoExtrato(); // NOVO

}

function gerarUltimos7Dias(){

    const dias = [];

    for(let i = 6; i >= 0; i--){

        const data = new Date();

        data.setDate(
            data.getDate() - i
        );

        dias.push(
            data.toLocaleDateString("pt-BR")
        );
    }

    return dias;
}

function renderGraficoExtrato(){

    const container =
        document.getElementById("graficoExtrato");

    if(!container) return;

    if(
        window.graficoExtrato &&
        typeof window.graficoExtrato.destroy === "function"
    ){
        window.graficoExtrato.destroy();
    }

    let entradas = 0;
    let saidas = 0;

    const movimentos = extrato.filter(item => {

        if(item.tipo === "Saldo inicial"){
            return false;
        }

        if(filtroAtual === "todos"){
            return true;
        }

        if(
            filtroAtual === "pix" &&
            item.tipo.includes("Pix")
        ){
            return true;
        }

        if(
            filtroAtual === "cartao" &&
            (
                item.tipo.includes("crédito") ||
                item.tipo.includes("fatura")
            )
        ){
            return true;
        }

        if(
            filtroAtual === "invest" &&
            (
                item.tipo.includes("Invest") ||
                item.tipo.includes("Resgate")
            )
        ){
            return true;
        }

        return false;
    });

    movimentos.forEach(item => {

        if(item.valor >= 0){
            entradas += Number(item.valor);
        }else{
            saidas += Math.abs(Number(item.valor));
        }

    });

    const resultado = entradas - saidas;

    if(
    entradas === 0 &&
    saidas === 0
){

    container.innerHTML = `
        <div style="
            height:350px;
            display:flex;
            align-items:center;
            justify-content:center;
            color:#94a3b8;
        ">
            Nenhuma movimentação para o filtro selecionado
        </div>
    `;

    document.getElementById(
        "resumoValores"
    ).innerHTML = `
        <div class="resumo-item">
            <h4>Entradas</h4>
            <strong class="entrada">
                R$ 0,00
            </strong>
        </div>

        <div class="resumo-item">
            <h4>Saídas</h4>
            <strong class="saida">
                R$ 0,00
            </strong>
        </div>

        <div class="resumo-item">
            <h4>Resultado</h4>
            <strong class="resultado">
                R$ 0,00
            </strong>
        </div>
    `;

    return;
}

    window.graficoExtrato = new ApexCharts(
        container,
        {
            chart:{
                type:'donut',
                height:350
            },

            series:[
                entradas,
                saidas
            ],

            labels:[
                'Entradas',
                'Saídas'
            ],

            colors:[
                '#22c55e',
                '#ef4444'
            ],

            legend:{
                position:'bottom',
                labels:{
                    colors:'#fff'
                }
            },

            dataLabels:{
                enabled:true
            },

            plotOptions: {
    pie: {
        donut: {
            size: '70%',

            labels: {

                show: true,

                name: {
                    show: true
                },

                value: {
                    show: true,
                    formatter: function () {
                        return formatarMoeda(saldo);
                    }
                },

                total: {
                    show: true,
                    label: 'Saldo Total',

                    formatter: function () {
                        return formatarMoeda(
 saldo +
        (cartao.limite - cartao.usado)
);
                    }
                }
            }
        }
    }
},
            
            
tooltip: {
    theme: "dark",

    custom: function({series, seriesIndex, w}){

        const valor = series[seriesIndex];

        const total = series.reduce(
            (a,b) => a + b,
            0
        );

        const percentual =
            (
                valor / total * 100
            ).toFixed(1);

        const label =
            w.globals.labels[seriesIndex];

        return `
            <div style="
                padding:12px;
                background:#111827;
                color:#fff;
                border-radius:10px;
            ">
                <strong>${label}</strong>
                <br>
                ${formatarMoeda(valor)}
                <br>
                ${percentual}%
            </div>
        `;
    }
}

        }
    );

    window.graficoExtrato.render();

    document.getElementById(
        "resumoValores"
    ).innerHTML = `

        <div class="resumo-item">
            <h4>Entradas</h4>
            <strong class="entrada">
                ${formatarMoeda(entradas)}
            </strong>
        </div>

        <div class="resumo-item">
            <h4>Saídas</h4>
            <strong class="saida">
                ${formatarMoeda(saidas)}
            </strong>
        </div>

        <div class="resumo-item">
            <h4>Resultado</h4>
            <strong class="resultado">
                ${formatarMoeda(resultado)}
            </strong>
        </div>
        <div class="resumo-item">
    <h4>Saldo Conta</h4>
    <strong>
        ${formatarMoeda(saldo)}
    </strong>
</div>

<div class="resumo-item">
    <h4>Limite Disponível</h4>
    <strong>
        ${formatarMoeda(
            cartao.limite - cartao.usado
        )}
    </strong>
</div>
    `;
}
// API FAKE
function fakePixAPI(data) {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        id: "PIX" + Math.floor(Math.random() * 100000),
        data: new Date().toLocaleString(),
        ...data
      });
    }, 2000);
  });
}
function calcularResgate(investimento){

    const valorOriginal = Number(investimento.valor);

    // 5% de rendimento
    const rendimento = valorOriginal * 0.05;

    return valorOriginal + rendimento;

}
console.log(investimentosUsuario);
// EXECUTAR
async function executarPix() {
  show("loading");

  let response = await fakePixAPI(pixState);

  document.getElementById("rId").innerText = response.id;
  document.getElementById("rData").innerText = response.data;
  document.getElementById("rContato").innerText = response.contato;
  document.getElementById("rValor").innerText = "R$ " + response.valor;

  
  // ✅ Atualiza saldo
  saldo -= pixState.valor;

  // ✅ REGISTRA NO EXTRATO (AQUI ✅)
  extrato.push({
    tipo: "Pix enviado",
    descricao: pixState.contato,
    valor: -pixState.valor,
    data: new Date().toLocaleString()
  });

  // ✅ ATUALIZA TELA SE ESTIVER NO EXTRATO
  if(document.getElementById("listaExtrato")){
    renderExtrato();
  }


  show("t5");
}

async function pagarFatura(){

  if(cartao.fatura <= 0){
    alert("Sem fatura pendente");
    return;
  }

  if(cartao.fatura > saldo){
    alert("Saldo insuficiente");
    return;
  }

  let content = document.getElementById('content');

  content.innerHTML = `<div>Processando pagamento...</div>`;

  let resp = await fakePixAPI({
    tipo: "cartao",
    valor: cartao.fatura
  });

  // ATUALIZA
  saldo -= cartao.fatura;

  extrato.push({
    tipo: "Pagamento fatura",
    valor: -cartao.fatura,
    data: resp.data
  });

  cartao.usado = 0;
  cartao.fatura = 0;
  comprasCartao = [];

  content.innerHTML = `
    <h2>✅ Pagamento realizado</h2>

    <div class="card">
      <p>ID: ${resp.id}</p>
      <p>Valor: ${formatarMoeda(resp.valor)}</p>
      <p>Data: ${resp.data}</p>
    </div>

    <button class="btn" onclick="loadPage('dashboard')">
      Voltar
    </button>
  `;
}

// VOLTAR
function back(step) {
  if(step === 1) show("t1");
  if(step === 2) show("t2");
  if(step === 3) show("t3");
}

// RESET
function resetarPix() {
  pixState = {};
  show("t1");
}

function createProductCard(title, rendimento, minimo, vencimento, resgate){
  return `
    <div class="product-card">

      <div class="product-left">
        <span class="tag">inter</span>
        <h3>${title}</h3>
        <p class="yield">${rendimento}</p>
      </div>

      <div class="product-details">
        <div>✅ FGC</div>
        <div>💸 Isento</div>
        <div>Mín: ${minimo}</div>
        <div>Venc: ${vencimento}</div>
        <div>Resgate: ${resgate}</div>
      </div>

      <div class="product-action">
        <button class="btn" onclick="openProduto('${title}')">
          Investir
        </button>
      </div>

    </div>
  `;
}
function comprarCartao(nome, valor){

  valor = Number(valor);

  if(!nome){
    alert("Digite o nome da compra");
    return;
  }

  if(!valor || valor <= 0){
    alert("Valor inválido");
    return;
  }

  let limiteDisponivel = cartao.limite - cartao.usado;

  if(valor > limiteDisponivel){
    alert("Limite insuficiente");
    return;
  }

  let compra = {
    id: comprasCartao.length + 1,
    nome: nome,
    valor: valor,
    data: new Date().toLocaleString()
  };

  comprasCartao.push(compra);

  cartao.usado += valor;
  cartao.fatura += valor;

extrato.push({
    tipo: "Compra crédito",
    descricao: nome,
    valor: -valor,
    data: new Date().toLocaleString()
});

  // EXTRATO
 
  if(document.getElementById("listaExtrato")){
    renderExtrato();
  }


  alert("✅ Compra realizada!");

  loadPage('cartao');
}
/* ✅ FUNÇÃO GLOBAL (AGORA FUNCIONA) */
function resgatar(id){

    console.log("Resgatando ID:", id);

    const indice = investimentosUsuario.findIndex(
        item => Number(item.id) === Number(id)
    );

    if(indice === -1){
        alert("Investimento não encontrado");
        return;
    }

    const investimento = investimentosUsuario[indice];

    const valorResgate = calcularResgate(investimento);

    // Credita saldo
    saldo += valorResgate;

    // Adiciona no extrato
    extrato.push({
        tipo: "Resgate",
        descricao: investimento.titulo,
        valor: valorResgate,
        data: new Date().toLocaleString()
    });

    // Remove da carteira
    investimentosUsuario.splice(indice, 1);

    alert(
        `✅ Resgate realizado!\n\n` +
        `Investimento: ${investimento.titulo}\n` +
        `Valor creditado: ${formatarMoeda(valorResgate)}`
    );

    loadPage("dashboard");
}

function formatarMoeda(valor){
  return valor.toLocaleString('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  });
}



function openProduto(nome){

  let content = document.getElementById('content');

  content.innerHTML = `

    <div class="card product-header">
      <div>
        <span class="tag">inter</span>
        <h2>${nome}</h2>
        <p class="yield">Rende até 91% do CDI</p>
      </div>

      <div class="header-info">
        <div>✅ FGC</div>
        <div>💸 Isento</div>
        <div>Mín: R$ 50</div>
        <div>Código: ABC123</div>
        <div>Resgate: Após 185 dias</div>
      </div>
    </div>

    <div class="invest-input">
      <p>Quanto deseja investir?</p>
      <h1 id="valorInvest">R$ 0,00</h1>
      <input type="number" oninput="updateValor(this.value)">
    </div>

    <div class="grid-2">

      <div class="card">
        <h3>Características</h3>
        <p>Emissor: Banco Inter</p>
        <p>Rentabilidade: 91% CDI</p>
        <p>Vencimento: 22/06/2031</p>
      </div>

      <div class="card">
        <h3>Rentabilidade</h3>
        <p>Até 250k → 89%</p>
        <p>Até 500k → 90%</p>
        <p>Acima → 91%</p>
      </div>

    </div>

    <div class="actions">
      <button class="btn-outline" onclick="loadPage('invest')">Voltar</button>
      <button class="btn" onclick="finalizarInvestimento('${nome}')">
  Investir
</button>

    </div>

  `;
}

/* ✅ VALOR DINÂMICO */
function atualizarFiltroAtivo(){

    document.querySelectorAll(".filtro-btn").forEach(btn => {

        btn.classList.remove("filtro-ativo");

    });

    const mapa = {
        todos: "btnTodos",
        pix: "btnPix",
        cartao: "btnCartao",
        invest: "btnInvest"
    };

    const ativo = document.getElementById(
        mapa[filtroAtual]
    );

    if(ativo){
        ativo.classList.add("filtro-ativo");
    }
}


function updateValor(valor){
  let formatado = Number(valor || 0).toLocaleString('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  });

  document.getElementById("valorInvest").innerText = formatado;
}
function finalizarInvestimento(nome){

  let valor = Number(
    document.querySelector(".invest-input input").value
  );

  if(!valor || valor <= 0){
    alert("Informe um valor válido");
    return;
  }

  if(valor > saldo){
    alert("Saldo insuficiente");
    return;
  }

  saldo -= valor;

  investimentosUsuario.push({
    id: Date.now(),
    titulo: nome,
    vencimento: "01/01/2030",
    valor: valor,
    dataAplicacao: new Date().toISOString()
  });

  extrato.push({
    tipo: "Investimento",
    descricao: nome,
    valor: -valor,
    data: new Date().toLocaleString()
  });

  
  alert("✅ Investimento realizado com sucesso");

  loadPage("dashboard");
}
/*Extrato */
function renderExtrato(){

  let lista = extrato
    .slice()
    .sort((a,b)=> new Date(b.data) - new Date(a.data))
    
.filter(item => {

        if(filtroAtual === "todos"){
            return true;
        }

        if(
            filtroAtual === "pix" &&
            item.tipo.includes("Pix")
        ){
            return true;
        }

        if(
            filtroAtual === "cartao" &&
            (
                item.tipo.includes("crédito") ||
                item.tipo.includes("fatura") ||
                item.tipo.includes("Cartão")
            )
        ){
            return true;
        }

        if(
            filtroAtual === "invest" &&
            (
                item.tipo.includes("Invest") ||
                item.tipo.includes("Resgate")
            )
        ){
            return true;
        }

        return false;

    });


  let html = "";

  if(lista.length === 0){
    html = `<p style="text-align:center;">Nenhuma movimentação</p>`;
  }

  lista.forEach(item => {
    let cor = item.valor < 0 ? "#ef4444" : "#22c55e";

    html += `
      <div class="card">
        <div class="row">
          <strong>${item.tipo}</strong>
          <span style="color:${cor}">
            ${formatarMoeda(item.valor)}
          </span>
        </div>

        <div class="row">
          <small>${item.descricao || "-"}</small>
          <small>${item.data}</small>
        </div>
      </div>
    `;
  });

  let el = document.getElementById("listaExtrato");
  if(el) el.innerHTML = html;
}



function renderGrafico(){

  let pix = 0;
  let cartao = 0;
  let invest = 0;

  extrato.forEach(item => {

    if(item.valor < 0){

      if(item.tipo.includes("Pix")) pix += Math.abs(item.valor);
      else if(item.tipo.includes("crédito")) cartao += Math.abs(item.valor);
      else invest += Math.abs(item.valor);
    }

  });

  let canvas = document.getElementById('grafico');

  if(!canvas) return;

  let ctx = canvas.getContext('2d');

  let total = pix + cartao + invest || 1;

  let dados = [
    {valor: pix, cor:"#22c55e"},
    {valor: cartao, cor:"#8b5cf6"},
    {valor: invest, cor:"#f59e0b"}
  ];

  let x = 0;

  dados.forEach(d => {
    let largura = (d.valor / total) * canvas.width;
    ctx.fillStyle = d.cor;
    ctx.fillRect(x, 0, largura, canvas.height);
    x += largura;
  });
}

/* INICIAL */
loadPage('dashboard');
