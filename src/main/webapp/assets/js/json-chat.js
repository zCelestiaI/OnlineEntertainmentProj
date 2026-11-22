var username = null;
var websocket = null;
var socketUrl = null;
var connectionWarningShown = false;

function setStatus(text) {
    var status = document.getElementById('chat-status');
    if (status) {
        status.textContent = text;
    }
}

function setSendEnabled(enabled) {
    var button = document.getElementById('send-button');
    if (button) {
        button.classList.toggle('btn-primary', enabled);
        button.classList.toggle('btn-secondary', !enabled);
    }
}

function appendSystemMessage(text) {
    var output = document.getElementById('messages');
    var line = document.createElement('p');
    line.className = 'chat-message system';
    line.textContent = text;
    output.appendChild(line);
    output.scrollTop = output.scrollHeight;
}

function init() {
    var body = document.body;
    username = body.dataset.username;

    var protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    var contextPath = body.dataset.contextPath || '';
    socketUrl = protocol + '//' + window.location.host + contextPath + '/json/chat/' + encodeURIComponent(username);
    setStatus('Connecting...');
    appendSystemMessage('Connecting to ' + socketUrl);
    websocket = new WebSocket(socketUrl);
    setSendEnabled(false);

    window.setTimeout(function() {
        if (websocket && websocket.readyState === WebSocket.CONNECTING) {
            setStatus('Still connecting...');
            appendSystemMessage('The chat server is still not responding. Check that your server supports WebSocket and that this endpoint is deployed: ' + socketUrl);
        }
    }, 4000);

    websocket.onopen = function(resp) {
        setStatus('Connected');
        setSendEnabled(true);
        console.log("onopen", resp);
    };

    websocket.onmessage = function(resp) {
        var msg = JSON.parse(resp.data);
        var output = document.getElementById('messages');
        var line = document.createElement('p');
        line.className = msg.type === 2 ? 'chat-message' : 'chat-message system';

        if (msg.type === 2) {
            var sender = document.createElement('b');
            sender.textContent = msg.sender;
            line.appendChild(sender);
            line.appendChild(document.createTextNode(': ' + msg.text));
        } else {
            line.textContent = msg.sender + ' ' + msg.text;
            document.getElementById('client-count').textContent = 'Chatters: ' + msg.count;
        }

        output.appendChild(line);
        output.scrollTop = output.scrollHeight;
        console.log("onmessage", resp.data);
    };

    websocket.onerror = function(resp) {
        setStatus('Connection error');
        appendSystemMessage('Cannot connect to chat server at ' + socketUrl);
        console.log("onerror", resp);
    };

    websocket.onclose = function(resp) {
        setStatus('Disconnected');
        appendSystemMessage(resp.reason || 'Disconnected from chat.');
        console.log("onclose", resp);
    };
}

function send() {
    var input = document.getElementById("message");
    var text = input.value.trim();

    if (!text) {
        return;
    }

    if (!websocket || websocket.readyState !== WebSocket.OPEN) {
        if (!connectionWarningShown) {
            appendSystemMessage('Chat is not connected yet. Current status: ' + socketStateName(websocket) + '.');
            appendSystemMessage('Socket URL: ' + socketUrl);
            connectionWarningShown = true;
        }
        return;
    }

    var msg = {
        sender: username,
        text: text,
        type: 2,
        count: null
    };
    websocket.send(JSON.stringify(msg));
    input.value = '';
    input.focus();
}

function socketStateName(socket) {
    if (!socket) {
        return 'not created';
    }
    if (socket.readyState === WebSocket.CONNECTING) {
        return 'connecting';
    }
    if (socket.readyState === WebSocket.OPEN) {
        return 'open';
    }
    if (socket.readyState === WebSocket.CLOSING) {
        return 'closing';
    }
    if (socket.readyState === WebSocket.CLOSED) {
        return 'closed';
    }
    return 'unknown';
}

document.addEventListener('DOMContentLoaded', function() {
    init();

    document.getElementById('message').addEventListener('keydown', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            send();
        }
    });
});
