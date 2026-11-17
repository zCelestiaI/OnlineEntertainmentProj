<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Community Chat - Online Entertainment</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        .chat-shell {
            max-width: 900px;
            margin: 0 auto;
        }

        .chat-panel {
            border: 1px solid rgba(0, 0, 0, 0.08);
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 4px 14px rgba(0, 0, 0, 0.08);
        }

        .chat-header {
            background: #212529;
            color: #fff;
            padding: 16px 20px;
        }

        .chat-messages {
            height: 420px;
            overflow-y: auto;
            padding: 18px;
            background: #f8f9fa;
        }

        .chat-message {
            margin-bottom: 12px;
            padding: 10px 12px;
            border-radius: 8px;
            background: #fff;
            border: 1px solid rgba(0, 0, 0, 0.06);
        }

        .chat-message.system {
            background: #fff8e1;
            color: #6c5200;
            font-style: italic;
        }

        .chat-form {
            padding: 16px;
            background: #fff;
            border-top: 1px solid rgba(0, 0, 0, 0.08);
        }

        body.dark-mode .chat-panel,
        body.dark-mode .chat-message {
            border-color: #333;
        }

        body.dark-mode .chat-messages,
        body.dark-mode .chat-form {
            background: #181818;
        }

        body.dark-mode .chat-message {
            background: #242424;
        }

        body.dark-mode .chat-message.system {
            background: #332b13;
            color: #ffd75e;
        }
    </style>
</head>
<body data-context-path="${pageContext.request.contextPath}" data-username="${currentUser.id}">
<jsp:include page="../layout/menu.jsp" />

<div class="container py-4">
    <div class="chat-shell">
        <div class="chat-panel">
            <div class="chat-header d-flex justify-content-between align-items-center">
                <div>
                    <h1 class="h4 mb-1">Community Chat</h1>
                    <div class="small text-white-50">Signed in as ${currentUser.fullname}</div>
                </div>
                <div class="text-end">
                    <span id="client-count" class="badge text-bg-warning">Chatters: 0</span>
                    <div id="chat-status" class="small text-white-50 mt-1">Connecting...</div>
                </div>
            </div>

            <div id="messages" class="chat-messages" aria-live="polite"></div>

            <div class="chat-form">
                <div class="input-group">
                    <input id="message" class="form-control" autocomplete="off" placeholder="Type a message...">
                    <button id="send-button" class="btn btn-primary" type="button" onclick="send()">
                        <i class="bi bi-send-fill me-1"></i>Send
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
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
        setSendEnabled(false);

        websocket = new WebSocket(socketUrl);

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
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
