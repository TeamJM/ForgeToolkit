package info.journeymap.forge_toolkit

import com.github.javaparser.ast.expr.StringLiteralExpr
import com.github.javaparser.ast.visitor.VoidVisitorAdapter

class Visitor(val strings: MutableSet<String>) : VoidVisitorAdapter<Void>() {
    override fun visit(sl: StringLiteralExpr, arg: Void?) {
        super.visit(sl, arg)
        val string = sl.asString()

        strings.add(string)

        if (string.startsWith("jm.")) {
            strings.add("$string.tooltip")
        }
    }
}
