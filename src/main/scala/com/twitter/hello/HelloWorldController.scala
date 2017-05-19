package com.twitter.hello

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import sangria.execution.Executor
import sangria.execution.deferred.DeferredResolver
import sangria.parser.QueryParser
import scala.concurrent.ExecutionContext.Implicits.global

import scala.util.{Failure, Success}

case class GraphQLRequest(
  query: String,
  operationName: Option[String]
)

class HelloWorldController extends Controller {

  get("/hi") { request: Request =>
    info("hi")
    "Hello " + request.params.getOrElse("name", "unnamed")
  }

  post("/hi") { hiRequest: HiRequest =>
    "Hello " + hiRequest.name + " with id " + hiRequest.id
  }

  get("/") { request: Request =>
    response.ok.file("graphiql.html")
  }

  post("/graphql") { request: GraphQLRequest =>
    QueryParser.parse(request.query) match {
      case Success(queryAst) => Executor.execute(
        SchemaDefinition.StarWarsSchema,
        queryAst,
        new CharacterRepo,
        deferredResolver = DeferredResolver.fetchers(SchemaDefinition.characters)
      )
      case Failure(error) => error
    }
  }
}
