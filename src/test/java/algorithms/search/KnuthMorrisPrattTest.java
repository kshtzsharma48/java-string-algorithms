package algorithms.search;

import algorithms.TestHelp;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static algorithms.TestHelp.timeAndLogMultipleCalls;
import static algorithms.TestHelp.timeMultipleCalls;

public class KnuthMorrisPrattTest extends StringSearchTestBase {

    @Override
    protected StringSearch createNewSearchApi() {
        return Searchers.newKnuthMorrisPrattStringSearcher();
    }
}
