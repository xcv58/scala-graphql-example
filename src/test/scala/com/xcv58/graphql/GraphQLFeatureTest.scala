package com.xcv58.graphql

import com.twitter.finagle.http.Status._

class GraphQLFeatureTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(new GraphQLServer)

//  test("Server#Say hi") {
//    server.httpGet(
//      path = "/hi?name=Bob",
//      andExpect = Ok,
//      withBody = "Hello Bob")
//  }
//
//  test("Server#Say hi for Post") {
//    server.httpPost(
//      path = "/hi",
//      postBody =
//        """
//        {
//          "id": 10,
//          "name" : "Sally"
//        }
//        """,
//      andExpect = Ok,
//      withBody = "Hello Sally with id 10")
//  }
}
