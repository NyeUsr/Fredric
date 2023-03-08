package nyeuser.fredric

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val setDefaultBtn = findViewById<Button>(R.id.configure)
        setDefaultBtn.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        val url = intent?.dataString
        if (url != null) {
            handleIntent(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } else {
            handleIntent(intent)
        }
    }

    private val services = mapOf(
        Service.YouTube to ServiceInfo(
            arrayOf("invidious.privacydev.net", "vid.puffyan.us", "inv.vern.cc", "invidious.kavin.rocks", "invidious.tiekoetter.com", "inv.riverside.rocks", "iv.ggtyler.dev", "invidious.nerdvpn.de"),
            arrayOf("^https?://(www\\.)?youtube\\.com/.*", "^https?://m\\.youtube\\.com/.*", "^https?://youtu\\.be/.*")),
        Service.Twitter to ServiceInfo(
            arrayOf("nitter.sneed.network", "canada.unofficialbird.com", "nitter.privacytools.io", "nitter.foss.wtf", "nitter.privacy.com.de", "nitter.1d4.us", "nitter.poast.org", "twitter.censors.us"),
            arrayOf("^https?://(mobile\\.)?twitter\\.com/.*", "^https?://twitter\\.com/.*")),
        Service.Reddit to ServiceInfo(
            arrayOf("libreddit.eu.org", "libreddit.spike.codes", "lr.odyssey346.dev", "rd.funami.tech", "libreddit.dcs0.hu", "lr.vern.cc", "www.troddit.com"),
            arrayOf("^https?://(www\\.)?reddit\\.com/.*")),
        Service.Imgur to ServiceInfo(
            arrayOf("rimgo.pussthecat.org", "rimgo.totaldarkness.net", "rimgo.vern.cc", "imgur.artemislena.eu", "rimgo.privacydev.net", "rimgo.bus-hit.me"),
            arrayOf("^https?://((i|i\\.stack)\\.)?imgur\\.com/.*")),
        Service.Instagram to ServiceInfo(
            arrayOf("bibliogram.froth.zone", "ig.tokhmi.xyz"),
            arrayOf("^https?://(www\\.)?instagram\\.com/.*")),
        Service.TikTok to ServiceInfo(
            arrayOf("proxitok.pussthecat.org", "tok.habedieeh.re", "tok.artemislena.eu", "proxitok.privacydev.net"),
            arrayOf("^https?://(www\\.)?tiktok\\.com/.*")),
        Service.IMDb to ServiceInfo(
            arrayOf("ld.vern.cc", "libremdb.esmailelbob.xyz", "lmdb.tokhmi.xyz", "libremdb.iket.me", "libremdb.pussthecat.org"),
            arrayOf("^https?://(www\\.)?imdb\\.com/.*")),
        Service.GoogleTranslate to ServiceInfo(
            arrayOf("simplytranslate.esmailelbob.xyz", "simplytranslate.org", "simplytranslate.manerakai.com", "translate.bus-hit.me", "translate.northboot.xyz", "translate.tiekoetter.com", "tl.vern.cc", "translate.slipfox.xyz"),
            arrayOf("^https?://translate\\.google\\.com/.*")),
        Service.Medium to ServiceInfo(
            arrayOf("scribe.rip", "scribe.nixnet.services", "scribe.citizen4.eu", "scribe.bus-hit.me", "scribe.froth.zone", "scribe.rawbit.ninja"),
            arrayOf("^https?://medium\\.com/.*")),
        Service.UrbanDict to ServiceInfo(
            arrayOf("rd.vern.cc", "ruraldictionary.esmailelbob.xyz"),
            arrayOf("^https?://(www\\.)?urbandictionary\\.com/.*")),
        Service.StackOverflow to ServiceInfo(
            arrayOf("code.whatever.social", "overflow.777.tf", "ao.vern.cc", "overflow.smnz.de", "anonymousoverflow.esmailelbob.xyz", "overflow.adminforge.de", "ao.foss.wtf", "overflow.hostux.net"),
            arrayOf("^https?://stackoverflow\\.com/.*")),
        Service.GoodReads to ServiceInfo(
            arrayOf("biblioreads.ml", "bl.vern.cc", "biblioreads.esmailelbob.xyz"),
            arrayOf("^https?://(www\\.)?goodreads\\.com/.*")),
        Service.Snopes to ServiceInfo(
            arrayOf("sd.vern.cc", "suds.esmailelbob.xyz"),
            arrayOf("^https?://(www\\.)?snopes\\.com/.*")),
        Service.Google to ServiceInfo(
            arrayOf("whoogle.hostux.net", "wg.vern.cc", "whoogle.privacydev.net", "whoogle.dcs0.hu", "search.sethforprivacy.com"),
            arrayOf("^https?://(www\\.)google\\.com/search.*")),
        Service.EnglishWiki to ServiceInfo(
            arrayOf("wiki.froth.zone", "wikiless.esmailelbob.xyz", "wikiless.northboot.xyz", "wl.vern.cc"),
            arrayOf("^https?://en\\.wikipedia\\.org/.*")),
        Service.GermanWiki to ServiceInfo(
            arrayOf("wiki.adminforge.de"),
            arrayOf("^https?://de\\.wikipedia\\.org/.*")),
        Service.Instructables to ServiceInfo(
            arrayOf("destructables.esmailelbob.xyz"),
            arrayOf("^https?://(www\\.)?instructables\\.com/.*")),
        Service.Reuters to ServiceInfo(
            arrayOf("neuters.de", "neuters.esmailelbob.xyz"),
            arrayOf("^https?://(www\\.)?reuters\\.com/.*")),
        Service.Odysee to ServiceInfo(
            arrayOf("lbry.projectsegfau.lt", "librarian.esmailelbob.xyz"),
            arrayOf("^https?://odysee\\.com/.*"))
    )

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_VIEW) {
            val url = intent.dataString
            Log.d("MainActivity", "Received intent with url: $url")
            services.forEach { (_, info) ->
                if (url != null) {
                    if (url.startsWith("https://youtu.be/")) {
                        val videoId = url.substringAfterLast("/", "")
                        val invidiousUrl = "https://${services[Service.YouTube]!!.instances.random()}/watch?v=$videoId"
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(invidiousUrl)))
                        finish()
                        return
                    }
                } else if (info.patterns.any { Regex(it).matches(url ?: "") }) {
                    try {
                        val newUrl = replaceHostnameWithRandomInstance(url, info.instances)
                        openUrlInDefaultBrowser(newUrl)
                        finish()
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error replacing hostname with random instance", e)
                    }
                    return
                }
            }
        }
    }
    private fun replaceHostnameWithRandomInstance(url: String?, instances: Array<String>): String? {
        if (url != null) {
            val parsedUrl = URL(url)
            val randomInstance = instances.random()
            return if (parsedUrl.query != null) {
                "${parsedUrl.protocol}://$randomInstance${parsedUrl.path}?${parsedUrl.query}"
            } else {
                "${parsedUrl.protocol}://$randomInstance${parsedUrl.path}"
            }
        }
        return null
    }

    private fun openUrlInDefaultBrowser(url: String?) {
        url?.let {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(it)
            intent.setPackage(null)
            startActivity(intent)
        }
    }
}

data class ServiceInfo(
    val instances: Array<String>,
    val patterns: Array<String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ServiceInfo

        if (!instances.contentEquals(other.instances)) return false
        if (!patterns.contentEquals(other.patterns)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = instances.contentHashCode()
        result = 31 * result + patterns.contentHashCode()
        return result
    }
}

enum class Service { YouTube, Twitter, Reddit, Imgur, Instagram, TikTok, IMDb, GoogleTranslate, Medium, UrbanDict, StackOverflow, GoodReads, Snopes, Google, EnglishWiki, GermanWiki, Instructables, Reuters, Odysee }