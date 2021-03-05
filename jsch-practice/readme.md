### java实现的web-ssh项目

### spring boot 2.4.2 + websocket + jsch 0.1.54 + xterm.js 

### 使用：
```
index.html：
  openTerminal( {
        operate:'connect',
        host: '需连接的IP地址',
        port: '端口',
        username: '用户名',
        password: '密码'
    });
webSsh.js：
  WSSHClient.prototype._endpoint = function () {
    return window.location.protocol === 'https:' ? 'wss://' : 'ws://' + '该服务ip:端口/web_ssh';
  };
```


##### xterm.js:https://xtermjs.org/

##### 参考：https://github.com/NoCortY/WebSSH
