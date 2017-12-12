package com.xcv58.graphql

import com.google.inject.Stage

class GraphQLStartupTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(
    twitterServer = new GraphQLServer,
    stage = Stage.PRODUCTION,
    verbose = false)

  test("Server#startup") {
    server.assertHealthy()
  }
}
