from py4j.java_gateway import *


class JvmContext:

    def __init__(self, port: int):
        self.gateway = JavaGateway(
            gateway_parameters=GatewayParameters(port=port),
            callback_server_parameters=CallbackServerParameters(port=0)
        )

        self.jvm = self.gateway.jvm

    def __enter__(self):
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.gateway.close_callback_server()
        self.gateway.close()
