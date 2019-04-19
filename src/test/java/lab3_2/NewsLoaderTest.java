package lab3_2;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.iis.mto.staticmock.Configuration;
import edu.iis.mto.staticmock.ConfigurationLoader;
import edu.iis.mto.staticmock.IncomingInfo;
import edu.iis.mto.staticmock.IncomingNews;
import edu.iis.mto.staticmock.NewsLoader;
import edu.iis.mto.staticmock.NewsReaderFactory;
import edu.iis.mto.staticmock.SubsciptionType;
import edu.iis.mto.staticmock.reader.NewsReader;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})

public class NewsLoaderTest {

    private Configuration config;
    private ConfigurationLoader configLoader;
    private NewsLoader newsLoader;
    private NewsReader newsReader;
    private IncomingNews incomingNews;

    @Before
    public void setUp() {
        newsLoader = new NewsLoader();
        config = new Configuration();
        incomingNews = new IncomingNews();

        mockStatic(ConfigurationLoader.class);
        mockStatic(NewsReaderFactory.class);

        configLoader = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configLoader);
        when(ConfigurationLoader.getInstance()
                                .loadConfiguration()).thenReturn(config);
        Whitebox.setInternalState(config, "readerType", "Any");

        newsReader = mock(NewsReader.class);
        when(NewsReaderFactory.getReader("Any")).thenReturn(newsReader);

        when(newsReader.read()).thenReturn(incomingNews);
    }

    @Test
    public void test() {
        incomingNews.add(new IncomingInfo("newsA", SubsciptionType.A));
        incomingNews.add(new IncomingInfo("newsB", SubsciptionType.B));
        incomingNews.add(new IncomingInfo("newsC", SubsciptionType.C));
        incomingNews.add(new IncomingInfo("newsNONE", SubsciptionType.NONE));

        newsLoader.loadNews();

        verify(configLoader, times(1)).loadConfiguration();
    }

}
