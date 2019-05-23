import edu.iis.mto.staticmock.*;
import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTest {

    private ConfigurationLoader configurationLoader;
    private NewsReaderFactory newsReaderFactory;
    private Configuration configuration;
    private NewsReader newsReader;
    private NewsLoader newsLoader;
    private PublishableNews publishableNews;
    private IncomingInfo incomingInfo;

    @Before
    public void init() {
        configuration = new Configuration();
        newsLoader = new NewsLoader();

        IncomingNews incomingNews = new IncomingNews();
        incomingNews.add(new IncomingInfo("a", SubsciptionType.A));
        incomingNews.add(new IncomingInfo("b", SubsciptionType.B));
        incomingNews.add(new IncomingInfo("c", SubsciptionType.C));
        incomingNews.add(new IncomingInfo("none", SubsciptionType.NONE));

        mockStatic(ConfigurationLoader.class);
        configurationLoader = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        when(ConfigurationLoader.getInstance().loadConfiguration()).thenReturn(configuration);

        newsReader = mock(NewsReader.class);
        when(newsReader.read()).thenReturn(incomingNews);

        mockStatic(NewsReaderFactory.class);
        newsReaderFactory = mock(NewsReaderFactory.class);
        when(NewsReaderFactory.getReader(configuration.getReaderType())).thenReturn(newsReader);
    }

    @Test
    public void loadNewsTestMethodWhereInListIsOnePublicInfoShouldReturnOne() {
        publishableNews = newsLoader.loadNews();
        List<String> publicMessages = Whitebox.getInternalState(publishableNews, "publicContent");
        Assert.assertThat("should return 1", publicMessages.size(), is(1));
    }
}
