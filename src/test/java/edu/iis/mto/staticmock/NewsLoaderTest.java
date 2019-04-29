package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;



@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTest {

    private ConfigurationLoader configurationLoaderMock;
    private Configuration configurationMock;
    private NewsReader newsReaderMock;
    private NewsLoader newsLoader;

    @Before
    public void setUp() {
        mockStatic(ConfigurationLoader.class);
        mockStatic(NewsReaderFactory.class);
        configurationLoaderMock = mock(ConfigurationLoader.class);
        configurationMock = mock(Configuration.class);
        newsReaderMock = mock(NewsReader.class);
        newsLoader = new NewsLoader();
    }

    @Test
    public void shouldVerifyIfConfigurationLoaderLoadConfigurationProperly() {
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoaderMock);
        when(configurationLoaderMock.loadConfiguration()).thenReturn(configurationMock);
        when(NewsReaderFactory.getReader(any())).thenReturn(newsReaderMock);
        when(newsReaderMock.read()).thenReturn(new IncomingNews());

        newsLoader.loadNews();

        verify(configurationLoaderMock).loadConfiguration();
    }
}
