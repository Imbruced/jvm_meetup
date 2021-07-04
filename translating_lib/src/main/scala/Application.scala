import py4j.GatewayServer

object Application {
  private val applicationPort = 8998
  def main(args: Array[String]): Unit = {
    val server: GatewayServer = new GatewayServer(this, applicationPort)
    server.start();
  }
}
