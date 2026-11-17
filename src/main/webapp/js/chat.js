// =================================
// SMART CHAT - JAVASCRIPT
// =================================

const messageInput = document.getElementById("userMessage");
const sendBtn = document.getElementById("sendBtn");
const chatMessages = document.getElementById("chatMessages");
const newChatBtn = document.getElementById("newChatBtn");

const contextPath = window.location.pathname.substring(
    0,
    window.location.pathname.indexOf("/", 1)
);

// Envoyer un message
async function sendMessage() {

    const message = messageInput.value.trim();

    if (message === "") {
        return;
    }

    // Supprimer le message de bienvenue
    const welcome = document.querySelector(".welcome-message");

    if (welcome) {
        welcome.remove();
    }

    // Afficher le message utilisateur
    addMessage(message, "user");

    messageInput.value = "";
    messageInput.style.height = "auto";

    // Afficher chargement
    const loading = addMessage("SMART CHAT réfléchit...", "bot");

    sendBtn.disabled = true;

    try {

        const formData = new URLSearchParams();
        formData.append("message", message);

        const response = await fetch(contextPath + "/chat", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
            },
            body: formData.toString()
        });

        const data = await response.json();

        loading.remove();

        if (!response.ok) {
            addMessage(data.error || "Une erreur est survenue.", "bot");
            return;
        }

        addMessage(data.reply, "bot");

    } catch (error) {

        loading.remove();

        addMessage(
            "Impossible de contacter SMART CHAT. Vérifiez votre connexion.",
            "bot"
        );

        console.error(error);

    } finally {

        sendBtn.disabled = false;
        messageInput.focus();

    }
}


// Ajouter un message dans la conversation
function addMessage(text, sender) {

    const messageDiv = document.createElement("div");

    messageDiv.style.padding = "15px";
    messageDiv.style.margin = "10px 0";
    messageDiv.style.borderRadius = "12px";
    messageDiv.style.maxWidth = "80%";
    messageDiv.style.whiteSpace = "pre-wrap";
    messageDiv.style.overflowWrap = "anywhere";

    if (sender === "user") {

        messageDiv.style.alignSelf = "flex-end";
        messageDiv.style.background = "#7f1d1d";
        messageDiv.style.color = "white";

    } else {

        messageDiv.style.alignSelf = "flex-start";
        messageDiv.style.background = "#27272a";
        messageDiv.style.color = "#f4f4f5";

    }

    // textContent évite l'interprétation de HTML envoyé par l'utilisateur
    messageDiv.textContent = text;

    chatMessages.appendChild(messageDiv);

    chatMessages.scrollTop = chatMessages.scrollHeight;

    return messageDiv;
}


// Bouton envoyer
sendBtn.addEventListener("click", sendMessage);


// Envoyer avec la touche Entrée
messageInput.addEventListener("keydown", function(event) {

    if (event.key === "Enter" && !event.shiftKey) {

        event.preventDefault();
        sendMessage();

    }

});


// Ajuster automatiquement la hauteur du champ
messageInput.addEventListener("input", function() {

    this.style.height = "auto";
    this.style.height = this.scrollHeight + "px";

});


// Nouvelle conversation
newChatBtn.addEventListener("click", function() {

    chatMessages.innerHTML = `
        <div class="welcome-message">
            <div class="welcome-icon">
                <i class="fa-solid fa-robot"></i>
            </div>

            <h1>Bonjour ! Bienvenue sur SMART CHAT.</h1>

            <p>
                Je suis votre assistant intelligent.
                Posez-moi une question, je suis prêt à vous aider !
            </p>
        </div>
    `;

});