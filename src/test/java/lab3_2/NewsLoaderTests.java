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
import edu.iis.mto.staticmock.IncomingNews;
import edu.iis.mto.staticmock.NewsLoader;
import edu.iis.mto.staticmock.NewsReaderFactory;
import edu.iis.mto.staticmock.reader.NewsReader;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTests {

    private NewsLoader newsLoader;
    private NewsReader newsReaderMock;
    private Configuration configuration;
    private ConfigurationLoader configurationLoaderMock;
    private IncomingNews incomingNews;

    @Before
    public void setUp() {
        newsLoader = new NewsLoader();
        configuration = new Configuration();
        mockStatic(ConfigurationLoader.class);
        mockStatic(NewsReaderFactory.class);
        configurationLoaderMock = mock(ConfigurationLoader.class);
        incomingNews = new IncomingNews();
        newsReaderMock = mock(NewsReader.class);
        String readerTypeValue = "readerType";
        Whitebox.setInternalState(configuration, "readerType", readerTypeValue);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoaderMock);
        when(ConfigurationLoader.getInstance()
                                .loadConfiguration()).thenReturn(configuration);
        when(NewsReaderFactory.getReader(readerTypeValue)).thenReturn(newsReaderMock);
        when(newsReaderMock.read()).thenReturn(incomingNews);
    }

    @Test
    public void shouldCallReadMethodOnce() {
        newsLoader.loadNews();
        verify(newsReaderMock, times(1)).read();
    }
}
