package com.xcv58.graphql.controller

import com.fasterxml.jackson.databind.JsonNode
import com.twitter.bijection.twitter_util.UtilBijections.twitter2ScalaFuture
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.json.FinatraObjectMapper
import com.xcv58.graphql.model.{CharacterRepo, SchemaDefinition}
import org.json4s.JsonAST.{JArray, JNull, JObject, JValue}
import org.json4s.jackson.JsonMethods.asJsonNode
import sangria.execution.Executor
import sangria.execution.deferred.DeferredResolver
import sangria.marshalling.json4s.jackson._
import sangria.parser.QueryParser

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}


case class GraphQLRequest(
  query: String,
  operationName: Option[String]
)

class GraphQLController extends Controller {

  // Basic JSON mapper
  val mapper: FinatraObjectMapper = FinatraObjectMapper.create()

  def removeObjectNulls(node: JValue): JValue = node match {
    // Strip out null values from the values of objects
    case JObject(fields) =>
      JObject(fields flatMap {
        case (k: String, JNull) => None
        case (k: String, v: JValue) => Some((k, removeObjectNulls(v)))
      })

    // We don't want to strip null values from the items in an array,
    // but the array might contain objects that contain nulls, so we recurse.
    case JArray(values) => JArray(values map removeObjectNulls)

    // Everything else is fine
    case _ => node
  }

  def toJson(node: Json4sJacksonResultMarshaller.Node): JsonNode =
    asJsonNode(removeObjectNulls(node))

  get("/") { _: Request =>
    response.ok.file("graphql-playground.html")
  }

  get("/graphql") { _: Request =>
    response.ok.file("graphql-playground.html")
  }

  post("/graphql") { request: GraphQLRequest =>
    QueryParser.parse(request.query) match {
      case Success(queryAst) =>
        twitter2ScalaFuture[JValue].inverse.apply(
          Executor.execute(
            SchemaDefinition.StarWarsSchema,
            queryAst,
            new CharacterRepo,
            deferredResolver = DeferredResolver.fetchers(SchemaDefinition.characters)
          )
        ).map(toJson)
      case Failure(error) => error
    }
  }
}