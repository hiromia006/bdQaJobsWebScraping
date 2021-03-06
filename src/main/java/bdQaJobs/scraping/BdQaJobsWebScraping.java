package bdQaJobs.scraping;

import bdQaJobs.util.FileWriteAndReader;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class BdQaJobsWebScraping {
    public static void main(String[] args) throws IOException {
        Set<String> jobs = new HashSet<>();
        Set<String> saveJobs = FileWriteAndReader.getSaveJobs();

        // Read text from file.
        String projectHomeDirectory = System.getProperty("user.dir");
        FileReader fr = new FileReader(projectHomeDirectory + "/src/main/resources/urls.txt");
        BufferedReader br = new BufferedReader(fr);
        String url;
        while ((url = br.readLine()) != null) {
            URL url1 = new URL(url);
            String baseUrl = url1.getProtocol() + "://" + url1.getAuthority();

            Connection connection = Jsoup.connect(getFinalLocation(url));
            if (url.contains("jobs.bdjobs.com")) {
                connection = connection
                        .cookie("JOBSRPP", "60");
            }

            Elements urlElements = connection
                    .execute()
                    .parse()
                    .select("a");
            String jobUrlWithTittle = "";
            for (Element urlElement : urlElements) {
                if (url.contains("bdjobs.com")) {
                    if (urlElement.text().trim().matches(".*(SQA|Assurance|QA|qa|sqa).*")) {
                        String jobUrl = urlElement.attr("href").trim();
                        jobUrl = !jobUrl.contains(baseUrl) ?
                                (!jobUrl.startsWith("/") ? baseUrl + "/" + jobUrl : baseUrl + jobUrl)
                                : jobUrl;

                        jobUrlWithTittle = urlElement.text().trim() + " =>> " + jobUrl;
                        if (!saveJobs.contains(jobUrlWithTittle)) {
                            jobs.add(jobUrlWithTittle);
                        }
                    }
                } else {
                    if (urlElement.attr("href").trim().matches(".*(SQA|Assurance|QA|qa|sqa).*")) {
                        String jobUrl = urlElement.attr("href").trim();
                        jobUrl = !jobUrl.contains(baseUrl) ?
                                (!jobUrl.startsWith("/") ? baseUrl + "/" + jobUrl : baseUrl + jobUrl)
                                : jobUrl;

                        jobUrlWithTittle = urlElement.text().trim() + " =>> " + jobUrl;
                        if (!saveJobs.contains(jobUrlWithTittle)) {
                            jobs.add(jobUrlWithTittle);
                        }
                    }
                }
            }

            jobs.forEach(System.out::println);

        }
        String source = projectHomeDirectory + "/src/main/resources/NewJobURL.txt";
        for (String job : jobs) {
            new File(source).deleteOnExit();
            FileWriteAndReader.WriteFile(job);
        }

        if (jobs.size() > 0) {
            String dest = projectHomeDirectory + "/src/main/resources/OldJobs.txt";
            FileWriteAndReader.copyWithoutOverWriting(source, dest);
        }

    }


    public static String getFinalLocation(String address) throws IOException {
        URL url = new URL(address);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            if (status == HttpURLConnection.HTTP_MOVED_TEMP
                    || status == HttpURLConnection.HTTP_MOVED_PERM
                    || status == HttpURLConnection.HTTP_SEE_OTHER) {
                String newLocation = conn.getHeaderField("Location");
                return getFinalLocation(newLocation);
            }
        }
        return address;
    }

    public static Connection.Response getConnection(String url) throws IOException {
        Connection connection = Jsoup.connect(url);
        if (url.contains("jobs.bdjobs.com")) {
            connection = connection
                    .cookie("JOBSRPP", "60");
        }
        return connection.execute();

    }

    public static Set<String> getQaJobs(Connection.Response response) throws IOException {
        Set<String> jobs = new HashSet<>();
        Elements elements = response.parse().select("a");
        for (Element element : elements) {
            if (element.text().trim().matches(".*(SQA|Assurance|QA|qa).*")) {
                jobs.add(element.text().trim() + "::" + element.attr("href").trim());
            }

        }
        return jobs;
    }
}
