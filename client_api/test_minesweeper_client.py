import pytest
import responses
import json

from .minesweeper_client import MinesweeperClient, MinesweeperClientException, Game

url = "http://mockserver:8080"


@pytest.fixture
def api_url():
    return url + "/minesweeper"


@pytest.fixture
def client():
    return MinesweeperClient(url)


@pytest.fixture
def headers():
    return {}


@responses.activate
def test_new_game_returns_created_game(client, api_url, headers):
    id = "b12551c1-ae98-46eb-9fcb-3fbca459c532"
    status = "Started"
    cells = [["C", "C", "C"], ["C", "C", "C"], ["C", "C", "C"]]
    response = {
        "id": id,
        "status": status,
        "duration": "1",
        "board": {"cells": cells}
    }
    responses.add(responses.POST, api_url, headers=headers, json=response, status=200)
    game = client.new_game(3, 3, 4)
    assert game.id == id
    assert game.data['status'] == status
    assert game.data['board']['cells'] == cells

@responses.activate
def test_new_game_throws_exception_if_status_code_not_ok(client, api_url, headers):
    response = {"error": "error message"}
    responses.add(responses.POST, api_url, headers=headers, json=response, status=400)
    with pytest.raises(MinesweeperClientException) as e:
        client.new_game()
    assert str(e.value) == "error message"

@responses.activate
def test_get_game_returns_the_game_if_found(client, api_url, headers):
    id = "b12551c1-ae98-46eb-9fcb-3fbca459c532"
    status = "Started"
    cells = [["C", "C", "C"], ["C", "C", "C"], ["C", "C", "C"]]
    response = {
        "id": id,
        "status": status,
        "duration": "1",
        "board": {"cells": cells}
    }
    responses.add(responses.GET, api_url + "/" + id, headers=headers, json=response, status=200)
    game = client.get_game(id)
    assert game.id == id
    assert game.data['status'] == status
    assert game.data['board']['cells'] == cells


@responses.activate
def test_get_game_throws_exception_if_status_code_not_found(client, api_url, headers):
    id = "b12551c1-ae98-46eb-9fcb-3fbca459c532"
    response = {"error": "game not found"}
    responses.add(responses.GET, api_url + "/" + id, headers=headers, json=response, status=404)
    with pytest.raises(MinesweeperClientException):
        client.get_game(id)


@responses.activate
def test_uncover_returns_game_with_uncovered_cell(client, api_url, headers):
    id = "b12551c1-ae98-46eb-9fcb-3fbca459c532"
    game = _create_game(id)
    new_cells = [["U", "C", "C"], ["C", "C", "C"], ["C", "C", "C"]]
    response = {
        "id": id,
        "status": "Started",
        "duration": "100",
        "board": {"cells": new_cells}
    }
    responses.add(responses.PUT, api_url + "/" + id + "/uncover", headers=headers, json=response, status=200)
    changed_game = client.uncover(game, 0, 0)
    assert changed_game.id == id
    assert changed_game.data['board']['cells'] == new_cells


@responses.activate
def test_set_red_flag_returns_game_with_flagged_cell(client, api_url, headers):
    id = "b12551c1-ae98-46eb-9fcb-3fbca459c533"
    game = _create_game(id)
    new_cells = [["R", "C", "C"], ["C", "C", "C"], ["C", "C", "C"]]
    response = {
        "id": id,
        "status": "Started",
        "duration": "100",
        "board": {"cells": new_cells}
    }
    responses.add(responses.PUT, api_url + "/" + id + "/red_flag", headers=headers, json=response, status=200)
    changed_game = client.set_red_flag(game, 0, 0)
    assert changed_game.id == id
    assert changed_game.data['board']['cells'] == new_cells


@responses.activate
def test_remove_red_flag_returns_game_without_flagged_cell(client, api_url, headers):
    id = "b12551c1-ae98-46eb-9fcb-3fbca459c533"
    game = _create_game(id)
    new_cells = [["C", "C", "C"], ["C", "C", "C"], ["C", "C", "C"]]
    response = {
        "id": id,
        "status": "Started",
        "duration": "100",
        "board": {"cells": new_cells}
    }
    responses.add(responses.DELETE, api_url + "/" + id + "/red_flag", headers=headers, json=response, status=200)
    changed_game = client.remove_red_flag(game, 0, 0)
    assert changed_game.id == id
    assert changed_game.data['board']['cells'] == new_cells


@responses.activate
def test_set_question_mark_returns_game_with_question_marked_cell(client, api_url, headers):
    id = "b12551c1-ae98-46eb-9fcb-3fbca459c533"
    game = _create_game(id)
    new_cells = [["?", "C", "C"], ["C", "C", "C"], ["C", "C", "C"]]
    response = {
        "id": id,
        "status": "Started",
        "duration": "100",
        "board": {"cells": new_cells}
    }
    responses.add(responses.PUT, api_url + "/" + id + "/question_mark", headers=headers, json=response, status=200)
    changed_game = client.set_question_mark(game, 0, 0)
    assert changed_game.id == id
    assert changed_game.data['board']['cells'] == new_cells


@responses.activate
def test_remove_question_mark_returns_game_without_question_marked_cell(client, api_url, headers):
    id = "b12551c1-ae98-46eb-9fcb-3fbca459c533"
    game = _create_game(id)
    new_cells = [["C", "C", "C"], ["C", "C", "C"], ["C", "C", "C"]]
    response = {
        "id": id,
        "status": "Started",
        "duration": "100",
        "board": {"cells": new_cells}
    }
    responses.add(responses.DELETE, api_url + "/" + id + "/question_mark", headers=headers, json=response, status=200)
    changed_game = client.remove_question_mark(game, 0, 0)
    assert changed_game.id == id
    assert changed_game.data['board']['cells'] == new_cells


def _create_game(id):
    status = "Started"
    cells = [["C", "C", "C"], ["C", "C", "C"], ["C", "C", "C"]]
    data = {
        "id": id,
        "status": status,
        "duration": "100",
        "board": {"cells": cells}
    }
    return Game(data)
