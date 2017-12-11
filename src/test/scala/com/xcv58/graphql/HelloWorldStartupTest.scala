package com.xcv58.graphql

import com.google.inject.Stage

class HelloWorldStartupTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(
    twitterServer = new HelloWorldServer,
    stage = Stage.PRODUCTION,
    verbose = false)

  test("Server#startup") {
    server.assertHealthy()
  }
}
