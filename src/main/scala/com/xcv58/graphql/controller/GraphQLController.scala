package com.xcv58.graphql.controller

import com.twitter.bijection.twitter_util.UtilBijections.twitter2ScalaFuture
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.xcv58.graphql.JsonUtils
import com.xcv58.graphql.model.{CharacterRepo, SchemaDefinition}
import org.json4s.JsonAST.JValue
import sangria.execution.Executor
import sangria.execution.deferred.DeferredResolver
import sangria.marshalling.json4s.jackson._
import sangria.parser.QueryParser

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}


case class GraphQLRequest(
  query: String,
  operationName: Option[String],
  variables: Option[Map[String, Any]]
)

class GraphQLController extends Controller {
  get("/") { _: Request =>
    response.ok.file("graphql-playground.html")
  }

  get("/graphql") { _: Request =>
    response.ok.file("graphql-playground.html")
  }

  post("/graphql") { request: GraphQLRequest =>
    val variables: JValue = JsonUtils.toJValue(request.variables)
    QueryParser.parse(request.query) match {
      case Success(queryAst) =>
        twitter2ScalaFuture[JValue].inverse.apply(
          Executor.execute(
            SchemaDefinition.StarWarsSchema,
            queryAst,
            new CharacterRepo,
            variables = variables,
            deferredResolver = DeferredResolver.fetchers(SchemaDefinition.characters)
          )
        ).map(JsonUtils.toJson)
      case Failure(error) => error
    }
  }
}
