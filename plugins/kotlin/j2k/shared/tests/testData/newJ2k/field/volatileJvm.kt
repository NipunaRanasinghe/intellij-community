// API_VERSION: 1.8
internal class A {
    @Volatile
    var field1: Int = 0

    @Volatile
    private var field2 = ""

    init {
        field2 = "new"
    }
}
