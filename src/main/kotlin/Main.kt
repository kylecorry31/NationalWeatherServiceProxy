import com.kylecorry.nationalweatherservice.NationalWeatherServiceProxy
import kotlinx.coroutines.runBlocking

fun main() {
    val proxy = NationalWeatherServiceProxy("NWS Proxy")
    runBlocking {
        val alerts = proxy.getAlerts(40.7128, -74.0060)
        println(alerts)
    }
}