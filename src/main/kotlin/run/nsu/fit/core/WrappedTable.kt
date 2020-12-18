package run.nsu.fit.core

class WrappedTable(): Table() {
     var wrappedName: String = ""

     override fun getName(): String {
          return wrappedName
     }
}