function WSSHClient() {
}
WSSHClient.prototype._endpoint = function () {
    return window.location.protocol === 'https:' ? 'wss://' : 'ws://' + '192.168.2.132:9303/web_ssh';
};

WSSHClient.prototype.connect = function (options) {
    var endpoint = this._endpoint();
    if (window.WebSocket) {
        //如果支持websocket
        this._connection = new WebSocket(endpoint);
    }else {
        //否则报错
        options.onError('WebSocket Not Supported');
        return;
    }

    this._connection.onopen = function () {
        options.onConnect();
    };

    this._connection.onmessage = function (evt) {
        var data = evt.data.toString();
        //data = base64.decode(data);
        options.onData(data);
    };

    this._connection.onclose = function (evt) {
        options.onClose();
    };
};

// 初始化连接
WSSHClient.prototype.sendInitData = function (options) {
    this._connection.send(JSON.stringify(options));
};

// 发送指令
WSSHClient.prototype.sendClientData = function (data) {
    this._connection.send(JSON.stringify({"operate": "command", "command": data}))
};
