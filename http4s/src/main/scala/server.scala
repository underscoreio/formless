package formless
package http4s

import org.http4s.server.blaze.BlazeBuilder

object Server extends App {
  BlazeBuilder.bindHttp(80, "0.0.0.0")
    .mountService(Service.service, "/")
    .run
    .awaitShutdown()
}
