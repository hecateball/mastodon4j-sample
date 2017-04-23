package mastodon4j;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import mastodon4j.entity.AccessToken;
import mastodon4j.entity.App;
import mastodon4j.entity.ClientCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hecateball
 */
public class PropertiesGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesGenerator.class);

    public static void main(String... args) throws Exception {
        URL location = PropertiesGenerator.class.getProtectionDomain().getCodeSource().getLocation();
        Path input = Paths.get(location.getPath(), "user.properties");
        Path temporary = Paths.get(location.getPath(), "mastodon4j.properties");
        try (InputStream inputStream = Files.newInputStream(input, StandardOpenOption.READ);
                OutputStream outputStream = Files.newOutputStream(temporary, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);) {
            // Loading user.properties
            Properties properties = new Properties();
            properties.load(inputStream);

            // Create temporary mastodon4j.properties
            Properties mastodonProperties = new Properties();
            mastodonProperties.setProperty("mastodon4j.uri", properties.getProperty("mastodon4j.uri"));
            mastodonProperties.store(outputStream, null);

            //Create Mastodon instance
            Mastodon mastodon = MastodonFactory.getInstance();

            // Generate client_id and client_secret
            App app = new App();
            app.setName(properties.getProperty("mastodon4j.clientName"));
            app.setWebsite(properties.getProperty("mastodon4j.website"));
            String redirectUris = properties.getProperty("mastodon4j.redirectUris");
            String scopes = properties.getProperty("mastodon4j.scopes");
            ClientCredential clientCredential = mastodon.apps().registerApplication(app, redirectUris, scopes);
            LOGGER.info("client_id:\t{}", clientCredential.getClientId());
            LOGGER.info("client_secret:\t{}", clientCredential.getClientSecret());

            // Generate access_token
            String emailAddress = properties.getProperty("mastodon4j.emailAddress");
            String password = properties.getProperty("mastodon4j.password");
            AccessToken accessToken = mastodon.oauth()
                    .issueAccessToken(clientCredential.getClientId(), clientCredential.getClientSecret(), emailAddress, password);
            LOGGER.info("access_token:\t{}", accessToken.getAccessToken());

            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(properties.getProperty("mastodon4j.outputPath"), "mastodon4j.properties"),
                    Charset.forName("UTF-8"), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                writer.append("mastodon4j.uri=" + properties.getProperty("mastodon4j.uri"));
                writer.newLine();
                writer.append("mastodon4j.accessToken=" + accessToken.getAccessToken());
                writer.newLine();
            }
            Files.deleteIfExists(temporary);
        }

    }

}
