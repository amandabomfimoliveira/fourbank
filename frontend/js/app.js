const API_BASE_URL = "http://localhost:8080/api";
const TOKEN_KEY = "fourbank.token";

const state = {
  token: sessionStorage.getItem(TOKEN_KEY),
  user: null,
  accounts: [],
  balanceVisible: true
};

const $ = (selector, parent = document) => parent.querySelector(selector);
const $$ = (selector, parent = document) => [...parent.querySelectorAll(selector)];
const setHidden = (element, hidden) => element.classList.toggle("hidden", hidden);

function formatMoney(value) {
  return new Intl.NumberFormat("pt-BR", { style: "currency", currency: "BRL" }).format(Number(value || 0));
}

function formatDocument(value = "") {
  const digits = value.replace(/\D/g, "");
  if (digits.length === 11) return digits.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4");
  if (digits.length === 14) return digits.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, "$1.$2.$3/$4-$5");
  return value;
}

function friendlyLabel(value = "") {
  const labels = { FISICA: "Pessoa física", JURIDICA: "Pessoa jurídica", CORRENTE: "Conta corrente", POUPANCA: "Conta poupança", ATIVA: "Ativa", BLOQUEADA: "Bloqueada", ENCERRADA: "Encerrada", USER: "Cliente", ADMIN: "Administrador" };
  return labels[value] || value.toLowerCase().replace(/^./, letter => letter.toUpperCase());
}

function escapeHtml(value) {
  return String(value ?? "").replace(/[&<>'"]/g, char => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", "'": "&#39;", '"': "&quot;" })[char]);
}

function showMessage(element, message, success = false) {
  element.textContent = message;
  element.classList.toggle("success-alert", success);
  setHidden(element, false);
}

function clearMessage(element) {
  element.textContent = "";
  element.classList.remove("success-alert");
  setHidden(element, true);
}

function parseApiError(body, fallback) {
  if (body?.campos && Object.keys(body.campos).length) return Object.values(body.campos)[0];
  return body?.mensagem || fallback;
}

async function api(path, options = {}) {
  const headers = { Accept: "application/json", ...options.headers };
  if (options.body) headers["Content-Type"] = "application/json";
  if (state.token) headers.Authorization = `Bearer ${state.token}`;
  let response;
  try {
    response = await fetch(`${API_BASE_URL}${path}`, { ...options, headers });
  } catch {
    throw new Error("Não foi possível conectar ao servidor. Confirme se o backend está rodando na porta 8080.");
  }
  const body = response.status === 204 ? null : await response.json().catch(() => null);
  if (!response.ok) {
    if (response.status === 401 && state.token && !path.startsWith("/auth/")) logout(false);
    throw new Error(parseApiError(body, `Não foi possível concluir a solicitação (${response.status}).`));
  }
  return body;
}

function openModal(modal) {
  setHidden(modal, false);
  document.body.classList.add("modal-open");
  requestAnimationFrame(() => $("input, select, button:not(.modal-close)", modal)?.focus());
}

function closeModals() {
  $$(".modal-backdrop").forEach(modal => setHidden(modal, true));
  document.body.classList.remove("modal-open");
}

function selectAuthTab(tab) {
  const login = tab === "login";
  $("#login-tab").classList.toggle("active", login);
  $("#register-tab").classList.toggle("active", !login);
  setHidden($("#login-form"), !login);
  setHidden($("#register-form"), login);
  $("#auth-title").textContent = login ? "Acesse sua conta" : "Abra sua conta grátis";
  $("#auth-description").textContent = login ? "Use seu e-mail e senha para continuar." : "Preencha seus dados e receba sua primeira conta agora.";
  clearMessage($("#auth-message"));
}

function openAuth(tab) {
  selectAuthTab(tab);
  openModal($("#auth-modal"));
}

async function authenticate(path, payload, submitButton) {
  const originalText = submitButton.textContent;
  submitButton.disabled = true;
  submitButton.textContent = path === "/auth/login" ? "Entrando..." : "Criando conta...";
  clearMessage($("#auth-message"));
  try {
    const auth = await api(path, { method: "POST", body: JSON.stringify(payload) });
    state.token = auth.token;
    sessionStorage.setItem(TOKEN_KEY, auth.token);
    window.location.href = "dashboard.html";
  } catch (error) {
    showMessage($("#auth-message"), error.message);
  } finally {
    submitButton.disabled = false;
    submitButton.textContent = originalText;
  }
}

async function loadCustomerArea() {
  const [user, accounts] = await Promise.all([api("/users/me"), api("/contas")]);
  state.user = user;
  state.accounts = accounts;
  renderDashboard();
  setHidden($("#public-view"), true);
  setHidden($("#dashboard-view"), false);
  window.scrollTo(0, 0);
}

function renderDashboard() {
  const firstName = state.user.nome.trim().split(/\s+/)[0];
  const initials = state.user.nome.trim().split(/\s+/).slice(0, 2).map(part => part[0]).join("").toUpperCase();
  $("#welcome-name").textContent = firstName;
  $("#profile-name").textContent = firstName;
  $("#user-initials").textContent = initials;
  $("#user-role").textContent = friendlyLabel(state.user.perfil);
  $("#active-accounts").textContent = state.accounts.filter(account => account.status === "ATIVA").length;
  const total = state.accounts.reduce((sum, account) => sum + Number(account.saldo), 0);
  const totalElement = $("#total-balance");
  totalElement.dataset.value = total;
  totalElement.textContent = state.balanceVisible ? formatMoney(total) : "R$ ••••••";
  $("#profile-data").innerHTML = `
    <div><dt>Nome</dt><dd>${escapeHtml(state.user.nome)}</dd></div>
    <div><dt>E-mail</dt><dd>${escapeHtml(state.user.email)}</dd></div>
    <div><dt>Documento</dt><dd>${escapeHtml(formatDocument(state.user.documento))}</dd></div>
    <div><dt>Cadastro</dt><dd>${friendlyLabel(state.user.tipoPessoa)}</dd></div>`;

  const list = $("#accounts-list");
  if (!state.accounts.length) {
    list.innerHTML = `<div class="empty-state">Você ainda não possui contas. Abra uma para começar.</div>`;
  } else {
    list.innerHTML = state.accounts.map(account => `
      <article class="account-card">
        <div class="account-card-header">
          <div class="account-type"><span class="account-type-icon">▰</span><div><h3>${friendlyLabel(account.tipo)}</h3><p>4BANK</p></div></div>
          <span class="status-pill ${account.status.toLowerCase()}">${friendlyLabel(account.status)}</span>
        </div>
        <div class="account-balance"><small>Saldo disponível</small><strong>${state.balanceVisible ? formatMoney(account.saldo) : "R$ ••••••"}</strong></div>
        <div class="account-details"><span>Agência<strong>${escapeHtml(account.agencia)}</strong></span><span>Conta<strong>${escapeHtml(account.numero)}</strong></span></div>
        ${account.status !== "ENCERRADA" ? `<div class="account-actions"><button class="account-action" type="button" data-account-action="${account.status === "BLOQUEADA" ? "desbloquear" : "bloquear"}" data-account-type="${account.tipo}">${account.status === "BLOQUEADA" ? "Desbloquear conta" : "Bloquear conta"}</button></div>` : ""}
      </article>`).join("");
  }

  const ownedTypes = new Set(state.accounts.filter(account => account.status !== "ENCERRADA").map(account => account.tipo));
  const availableTypes = ["CORRENTE", "POUPANCA"].filter(type => !ownedTypes.has(type));
  const newAccountButton = $("#new-account-button");
  newAccountButton.disabled = availableTypes.length === 0;
  newAccountButton.textContent = availableTypes.length ? "+ Nova conta" : "Todos os tipos contratados";
  $("#new-account-type").innerHTML = availableTypes.map(type => `<option value="${type}">${friendlyLabel(type)}</option>`).join("");
}

function logout(showPublic = true) {
  state.token = null;
  state.user = null;
  state.accounts = [];
  sessionStorage.removeItem(TOKEN_KEY);
  setHidden($("#dashboard-view"), true);
  setHidden($("#public-view"), false);
  if (showPublic) window.scrollTo(0, 0);
}

async function handleAccountStatus(action, type, button) {
  const originalText = button.textContent;
  button.disabled = true;
  button.textContent = action === "bloquear" ? "Bloqueando..." : "Desbloqueando...";
  try {
    await api(`/contas/${action}`, { method: "PATCH", body: JSON.stringify({ tipo: type }) });
    state.accounts = await api("/contas");
    renderDashboard();
    showMessage($("#dashboard-message"), `Conta ${action === "bloquear" ? "bloqueada" : "desbloqueada"} com sucesso.`, true);
  } catch (error) {
    showMessage($("#dashboard-message"), error.message);
    button.disabled = false;
    button.textContent = originalText;
  }
}

$$('[data-open-auth]').forEach(button => button.addEventListener("click", () => openAuth(button.dataset.openAuth)));
$$('[data-auth-tab]').forEach(button => button.addEventListener("click", () => selectAuthTab(button.dataset.authTab)));
$$('[data-close-modal]').forEach(button => button.addEventListener("click", closeModals));
$$('[data-focus-accounts]').forEach(button => button.addEventListener("click", () => $("#account-section").scrollIntoView({ behavior: "smooth" })));
$$('.modal-backdrop').forEach(modal => modal.addEventListener("click", event => { if (event.target === modal) closeModals(); }));
document.addEventListener("keydown", event => { if (event.key === "Escape") closeModals(); });

$("#login-form").addEventListener("submit", event => {
  event.preventDefault();
  authenticate("/auth/login", Object.fromEntries(new FormData(event.currentTarget)), $("button[type='submit']", event.currentTarget));
});

$("#register-form").addEventListener("submit", event => {
  event.preventDefault();
  const data = Object.fromEntries(new FormData(event.currentTarget));
  data.documento = data.documento.replace(/\D/g, "");
  authenticate("/auth/register", data, $("button[type='submit']", event.currentTarget));
});

$("#account-form").addEventListener("submit", async event => {
  event.preventDefault();
  const button = $("button[type='submit']", event.currentTarget);
  button.disabled = true;
  button.textContent = "Abrindo...";
  try {
    await api("/contas", { method: "POST", body: JSON.stringify({ tipo: $("#new-account-type").value }) });
    state.accounts = await api("/contas");
    renderDashboard();
    closeModals();
    showMessage($("#dashboard-message"), "Sua nova conta foi aberta com sucesso.", true);
  } catch (error) {
    $("#account-modal-description").textContent = error.message;
  } finally {
    button.disabled = false;
    button.textContent = "Abrir conta";
  }
});

$("#accounts-list").addEventListener("click", event => {
  const button = event.target.closest("[data-account-action]");
  if (button) handleAccountStatus(button.dataset.accountAction, button.dataset.accountType, button);
});

$("#toggle-balance").addEventListener("click", () => {
  state.balanceVisible = !state.balanceVisible;
  $("#toggle-balance").textContent = state.balanceVisible ? "◉" : "○";
  $("#toggle-balance").setAttribute("aria-label", state.balanceVisible ? "Ocultar saldo" : "Mostrar saldo");
  renderDashboard();
});

$("#profile-button").addEventListener("click", () => openModal($("#profile-modal")));
$("#new-account-button").addEventListener("click", () => { $("#account-modal-description").textContent = "Escolha um tipo que você ainda não possui."; openModal($("#account-modal")); });
$("#logout-button").addEventListener("click", () => logout());
$("#nav-toggle").addEventListener("click", event => { const isOpen = $("#site-nav").classList.toggle("open"); event.currentTarget.setAttribute("aria-expanded", String(isOpen)); });
$("#site-nav").addEventListener("click", () => { $("#site-nav").classList.remove("open"); $("#nav-toggle").setAttribute("aria-expanded", "false"); });
$("#sidebar-toggle").addEventListener("click", () => $(".dashboard-sidebar").classList.toggle("open"));

if (state.token) window.location.href = "dashboard.html";
