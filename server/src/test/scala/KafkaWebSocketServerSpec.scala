import java.net.InetSocketAddress
import java.nio.ByteBuffer

import org.java_websocket.WebSocket
import org.msgpack.MessagePack
import org.scalatest.mock.EasyMockSugar
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConverters._

class KafkaWebSocketServerSpec extends FlatSpec with Matchers with EasyMockSugar {

    "Receiving a message with a group id and topic" should "create a Kafka consumer for the connection" in {
        val msgPack = new MessagePack

        val map = Map(
            "command" -> "init",
            "groupId" -> "testing-group-id",
            "topics" -> List("testing-topic1", "testing-topic2").asJava)

        val message = msgPack.write(map.asJava)
        val conn = mock[WebSocket]

        val server = new KafkaWebSocketServer(new InetSocketAddress(9999))
        server.consumers.isEmpty should be(true)

        server.onMessage(conn, ByteBuffer.wrap(message))
        server.consumers.size should be(1)

        val (_, consumer) = server.consumers.head
        //consumer.topics should be(List("testing-topic1", "testing-topic2"))
    }

}
