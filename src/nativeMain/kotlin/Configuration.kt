class Configuration {
    fun getOrDefault(executable: String): Resolution {
        return Resolution(1920u, 1080u, 60u)
    }
}