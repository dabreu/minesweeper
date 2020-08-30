import requests
from beautifultable import BeautifulTable
from termcolor import colored

class Game:
    """ Game response class """

    CODES = {'M': "\U0001F4A3", 'F': "\U0001F6A9", 'Q': "\U00002753", 'C': "\U00002B1C"}

    def __init__(self, data):
        self.data = data

    @property
    def id(self):
        return self.data['id']

    def print(self):
        print('Game: {}'.format(self.data['status']))
        print('Time: {} secs'.format(self.data['duration']))
        board = self.get_printable_board()
        print(board)

    def get_printable_board(self):
        table = BeautifulTable()
        board = self.data['board']['cells']
        first_row = ["-"]
        row_size = len(board[0])
        first_row.extend(list(map(lambda x: colored(x, "blue"), range(row_size))))
        table.rows.append(first_row)
        row_num = 0
        for row in board:
            board_row = [colored(row_num, "blue")]
            for cell in row:
                if cell in self.CODES:
                    board_row.append(self.CODES[cell])
                else:
                    board_row.append(cell)
            table.rows.append(board_row)
            row_num += 1
        table.set_style(BeautifulTable.STYLE_BOX_ROUNDED)
        return table


class MinesweeperClientException(Exception):
    pass


class MinesweeperClient:
    """ Minesweeper client """

    def __init__(self, server_url):
        self._server_url = server_url

    def new_game(self, rows=10, columns=10, mines=10):
        """ creates a new board with the given dimensions and number of mines """
        response = requests.post(self._api_url(),
                                 headers=self._get_headers(),
                                 data={'rows': rows, 'columns': columns, 'mines': mines})
        return self._get_game(response)

    def get_game(self, id):
        """ gets an existing game """
        response = requests.get(self._api_url() + "/" + id, headers=self._get_headers())
        return self._get_game(response)

    def uncover(self, game, row, column):
        """ uncovers/reveals the cell of the game on the indicated position """
        response = self._put_request("uncover", game, row, column)
        return self._get_game(response)

    def set_red_flag(self, game, row, column):
        """ sets a red flag on the cell of the game on the indicated position """
        response = self._put_request("red_flag", game, row, column)
        return self._get_game(response)

    def remove_red_flag(self, game, row, column):
        """ removes a red flag on the cell of the game on the indicated position """
        response = self._delete_request("red_flag", game, row, column)
        return self._get_game(response)

    def set_question_mark(self, game, row, column):
        """ sets a question mark on the cell of the game on the indicated position """
        response = self._put_request("question_mark", game, row, column)
        return self._get_game(response)

    def remove_question_mark(self, game, row, column):
        """ removes a question mark on the cell of the game on the indicated position """
        response = self._delete_request("question_mark", game, row, column)
        return self._get_game(response)

    def _get_game(self, response):
        """ verifies the response and returns the game's information if there is no error """
        if response.status_code != requests.codes.ok:
            raise MinesweeperClientException(self._get_error(response))
        return Game(response.json())

    def _api_url(self):
        return self._server_url + "/minesweeper"

    @staticmethod
    def _get_error(response):
        return response.json()['error']

    def _put_request(self, action, game, row, column):
        return requests.put('{}/{}/{}'.format(self._api_url(), game.id, action),
                            headers=self._get_headers(),
                            data={'row': row, 'column': column})

    def _delete_request(self, action, game, row, column):
        return requests.delete('{}/{}/{}'.format(self._api_url(), game.id, action),
                               headers=self._get_headers(),
                               data={'row': row, 'column': column})

    @staticmethod
    def _get_headers():
        return {}
