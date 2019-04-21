package lab3_2;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

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

public class ConfigurationLoaderTest1 {

    public NewsReader newsReader;
    public NewsLoader newsLoader;
    public IncomingNews incomingNews;
    public Configuration configuration;
    public ConfigurationLoader configurationLoader;

    @Before
    public void setUp() {
        incomingNews = new IncomingNews();
        configuration = new Configuration();
        newsLoader = new NewsLoader();

        configurationLoader = mock(ConfigurationLoader.class);
        mockStatic(ConfigurationLoader.class);
        mockStatic(NewsReaderFactory.class);

        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);

        when(ConfigurationLoader.getInstance()
                                .loadConfiguration()).thenReturn(configuration);

        Whitebox.setInternalState(configuration, "readerType", "Any");

        newsReader = mock(NewsReader.class);
        when(NewsReaderFactory.getReader("Any")).thenReturn(newsReader);

        when(newsReader.read()).thenReturn(incomingNews);
    }

    @Test
    public void isLoadConfigurationOnes() {
        incomingNews.add(new IncomingInfo("NewNewsA", SubsciptionType.A));
        incomingNews.add(new IncomingInfo("NewNewsB", SubsciptionType.B));
        incomingNews.add(new IncomingInfo("NewNewsC", SubsciptionType.C));
        incomingNews.add(new IncomingInfo("newsNONE", SubsciptionType.NONE));

        newsLoader.loadNews();

        verify(configurationLoader, times(1)).loadConfiguration();
    }

}
