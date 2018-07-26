package at.medunigraz.imi.bst.trec.query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.medunigraz.imi.bst.trec.model.Result;
import at.medunigraz.imi.bst.trec.model.Topic;

public class WordRemovalQueryDecorator extends QueryDecorator {

	private static final Set<String> DOMAIN_STOPWORDS = new HashSet<>();
	static {
		DOMAIN_STOPWORDS.add("cancer");
		DOMAIN_STOPWORDS.add("carcinoma");
		DOMAIN_STOPWORDS.add("tumor");
		DOMAIN_STOPWORDS.add("amplification");
        DOMAIN_STOPWORDS.add("of");
        DOMAIN_STOPWORDS.add("the");
        DOMAIN_STOPWORDS.add("malignant");
        DOMAIN_STOPWORDS.add("neoplasm");
	};

	private static final String TOKEN_SEPARATOR = " ";

	public WordRemovalQueryDecorator(Query decoratedQuery) {
		super(decoratedQuery);
		readStopwords();
	}

	@Override
	public List<Result> query(Topic topic) {
		topic.withDisease(removeStopwords(topic.getDisease()));
		topic.withGene(removeStopwords(topic.getGene()));
		topic.diseaseSynonyms = removeStopwords(topic.diseaseSynonyms);
		return decoratedQuery.query(topic);
	}

	private void readStopwords() {
		// TODO read stopwords list from file
	}

	private List<String> removeStopwords(List<String> target) {
		List<String> ret = new ArrayList<>();
		target.forEach(e -> ret.add(removeStopwords(e)));
		return ret;
	}

	private String removeStopwords(String target) {
		String[] diseaseTokens = target.split(TOKEN_SEPARATOR);

		StringBuilder filteredTarget = new StringBuilder();
		for (String token : diseaseTokens) {
			if (!DOMAIN_STOPWORDS.contains(token.toLowerCase())) {
				filteredTarget.append(token);
				filteredTarget.append(TOKEN_SEPARATOR);
			}
		}
		
		return filteredTarget.toString().trim();
	}

}
