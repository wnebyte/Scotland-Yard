package se.kau.cs.sy.services.match.management;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import se.kau.cs.sy.match.Match;

public class MatchStorage {

	public static MatchStorage getInstance() {
		return instance;
	}

	private static MatchStorage instance = new MatchStorage();

	private Map<UUID, Match> matches;

	private Map<UUID, String> matchNames;
	
	private MatchStorage() {
		matches = new ConcurrentHashMap<>();
		matchNames = new ConcurrentHashMap<>();
	}

	public synchronized boolean add(Match match, String name) {
		boolean added = false;
		if (!matches.containsKey(match.getId())) {
			matches.put(match.getId(), match);
			matchNames.put(match.getId(), name);
			added = true;
		}
		return added;
	}
	
	public Match getMatch(UUID id) {
		return matches.get(id);
	}
	
	public String getName(UUID id) {
		return matchNames.get(id);
	}
	
	public boolean setName(UUID matchId, String name) {
		boolean modified = false;
		if (matchNames.containsKey(matchId)) {
			matchNames.put(matchId, name);
			modified = true;
		}
		return modified;
	}
	
	public Set<Match> getAllMatches() {
		Set<Match> allMatches = new HashSet<>(matches.values());
		return allMatches;
	}
}
