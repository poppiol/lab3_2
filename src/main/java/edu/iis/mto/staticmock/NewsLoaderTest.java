package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.FileNewsReader;
import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class) @PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class}) public class NewsLoaderTest {

    private ConfigurationLoader configurationLoader;
    private Configuration configuration;
    private NewsReader newsReader;
    private IncomingNews incomingNews;

    @Before public void init() {
        configuration = new Configuration();
        newsReader = new FileNewsReader();
        incomingNews = new IncomingNews();

        mockStatic(ConfigurationLoader.class);
        mockStatic(NewsReaderFactory.class);

        configurationLoader = mock(ConfigurationLoader.class);
        newsReader = mock(NewsReader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        when(ConfigurationLoader.getInstance().loadConfiguration()).thenReturn(configuration);

        when(NewsReaderFactory.getReader(any())).thenReturn(newsReader);
        when(newsReader.read()).thenReturn(incomingNews);

    }

    @Test public void oneCallLoadNewsShouldReadOnce() {
        NewsLoader newsLoader = new NewsLoader();
        newsLoader.loadNews();

        verify(newsReader, times(1)).read();
    }

    @Test public void oneCallLoadNewsShouldResadOnce() {
        PublishableNewsStub publishableNewsStub = PublishableNewsStub.create();

        publishableNewsStub.addPublicInfo("publicInfo1");
        publishableNewsStub.addPublicInfo("publicInfo2");
        publishableNewsStub.addPublicInfo("publicInfo3");

        Assert.assertThat(publishableNewsStub.getPublicContent().size(), is(equalTo(3)));
    }

}
