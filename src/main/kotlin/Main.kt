import com.kylecorry.nationalweatherservice.NationalWeatherServiceProxy
import kotlinx.coroutines.runBlocking

fun main() {
    val proxy = NationalWeatherServiceProxy("NWS Proxy")
    runBlocking {
        val forecast = proxy.getForecast(40.7128, -74.006)
        println(forecast)
    }
}