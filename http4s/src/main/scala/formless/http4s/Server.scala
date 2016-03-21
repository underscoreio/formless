package formless
package http4s

import org.http4s.server.blaze._

object Server {
  val builder = BlazeBuilder.mountService(Service.view)

  def serve() = builder.run.awaitShutdown()
}
