Pour cacher son mot de passe 

Créé le fichier .env et y mettre les variable d'environnement
Example:
//.env
DB_URL=jdbc:postgresql://localhost:5432/ma_base
DB_USER=postgres
DB_PASSWORD=votre_vrai_mot_de_passe
Puis dans pom.xml on ajoute la dépendance Dotenv qui permet de lire .env
//pom.xml
<dependency>
<groupId>io.github.cdimascio</groupId>
<artifactId>dotenv-java</artifactId>
<version>3.0.0</version>
</dependency>

Dans application.properties on ajoute ces ligne pour pouvoir récupérer les donné  de .env
//application.properties
db.url=${DB_URL:jdbc:postgresql://localhost:5432/ma_base}
db.user=${DB_USER}
db.password=${DB_PASSWORD}

Dans Application.java on ajoute ces lignes pour ex
//Application.java:
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

...

public static void main(String[] args) {
Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

...

Dans database 
    @Value("${db.url}")
    private String url;

    @Value("${db.user}")
    private String user;

    @Value("${db.password}")
    private String password;

