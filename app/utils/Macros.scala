package utils

import scala.reflect.macros.Context
import language.experimental.macros
import scala.reflect.runtime.universe._

object Macros {
  def concreteSubclassesInPackage[P](packageName: String): Seq[Symbol] = macro concreteSubclassesInPackage_impl[P]

  def concreteSubclassesInPackage_impl[P](c: Context)(packageName: c.Expr[String]): c.Expr[Seq[Symbol]] = {
    import c.universe._
    import c.mirror._

    val symbol = typeOf[P].typeSymbol
    symbol

    println(showRaw(packageName))
    val destinationPackage =
      packageName.tree match {
        case Literal(Constant(s: String)) => staticPackage(s)
        case _ => throw new Exception("only constants allowed")
      }
    //
    println(destinationPackage.getClass)
    println(showRaw(reify { destinationPackage.typeSignature }.tree))
    println(c.eval(c.Expr(Select(Ident(destinationPackage), "typeSignature"))))

    // val pack = staticPackage("scala.collection.immutable").typeSignature.declarations.toSeq filter (_.isType) map (_.asType.fullName)

    val filterFunc: c.Expr[(scala.reflect.runtime.universe.Symbol) => Boolean] =
      reify { (ts: scala.reflect.runtime.universe.Symbol) => ts.isClass && !ts.isAbstractOverride }
    c.Expr[Seq[scala.reflect.runtime.universe.Symbol]](
      Apply(
        Select(Select(Select(Select(Ident(destinationPackage), newTermName("typeSignature")), newTermName("declarations")),
          newTermName("toSeq")), newTermName("filter")),
        List(filterFunc.tree)
      )
    )
  }
}
