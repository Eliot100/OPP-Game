package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Server.Game_Server;
import Server.game_service;
import algorithms.Arena_Algo;
import dataStructure.Arena;
import gameClient.MyServer;

class Arena_AlgoTest {

	@Test
	void testGetFruitEdge() {
		game_service game = Game_Server.getServer(0);
		MyServer server = new MyServer(game);
		Arena arena = new Arena(server.getGraph(), server.getFruits(), server.getRobots());
		assertEquals(Arena_Algo.getFruitEdge(arena.getGraph(), arena.getFruits()[0]), 
				arena.getGraph().getEdge(9, 8));
		assertNotEquals(Arena_Algo.getFruitEdge(arena.getGraph(), arena.getFruits()[0]), 
				arena.getGraph().getEdge(0, 1));
	}

}
