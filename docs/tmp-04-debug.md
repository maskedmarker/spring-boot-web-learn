# debug相关的

## tomcat debug

```text
java标准库中,server-socket绑定监听端口的方法,只有如下2个方法:
java.net.ServerSocket.bind(java.net.SocketAddress, int)
java.nio.channels.ServerSocketChannel.bind(java.net.SocketAddress, int)

tomcat现在也是使用nio方法来处理networking,使用的是
java.nio.channels.ServerSocketChannel.bind(java.net.SocketAddress, int)
在该方法处打断点,即可看到整个调用链

```