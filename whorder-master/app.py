import datetime

from flask import Flask, request

from tqsdk import TqApi, TqKq, TqAuth, TqAccount

import order
from db_operating import get_trading_holding
from db_utils import db_connect
from util import is_trade_time

app = Flask(__name__)

user_api_map = {}


@app.route('/open', methods=['POST'])
def open():  # put application's code here
    data = request.json
    xinyi_account = data.get('xinyiAccount')
    xinyi_pwd = data.get("xinyiPwd")
    futures_company = data.get('company', None)
    trading_account = data.get("tradingAccount", None)
    trading_pwd = data.get("tradingPwd", None)
    uid = int(data.get("uid"))
    model_id = int(data.get("modelId"))
    code = data.get("code")
    open_num = int(data.get("lot"))
    price = data.get("price", None)
    buy_or_sell = int(data.get("bkOrSk"))
    if is_trade_time(datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")):
        api = user_api_map.get(uid, None)
        if api is None:
            if trading_account == '' and trading_pwd == '':  # 模拟交易
                api = TqApi(TqKq(), auth=TqAuth(xinyi_account, xinyi_pwd))
            else:  # 实盘交易
                api = TqApi(TqAccount(futures_company, trading_account, trading_pwd),
                            auth=TqAuth(xinyi_account, xinyi_pwd))
            user_api_map[uid] = api

        result = order.open(api, uid, model_id, code, buy_or_sell, open_num, price)
    else:
        result = {'code': 500}
    return result, result['code']


@app.route('/close', methods=['POST'])
def close():  # put application's code here
    data = request.json
    xinyi_account = data.get('xinyiAccount')
    xinyi_pwd = data.get("xinyiPwd")
    futures_company = data.get('company', None)
    trading_account = data.get("tradingAccount", None)
    trading_pwd = data.get("tradingPwd", None)
    uid = int(data.get("uid"))
    lot = int(data.get("lot"))
    price = data.get("price", None)
    model_id = int(data.get("modelId"))
    if is_trade_time(datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")):
        api = user_api_map.get(uid, None)
        if api is None:
            if trading_account == '' and trading_pwd == '':  # 模拟交易
                api = TqApi(TqKq(), auth=TqAuth(xinyi_account, xinyi_pwd))
            else:  # 实盘交易
                api = TqApi(TqAccount(futures_company, trading_account, trading_pwd),
                            auth=TqAuth(xinyi_account, xinyi_pwd))
            user_api_map[uid] = api
        result = order.close(api, model_id, lot, price)
    else:
        result = {'code':500}
    return result, result['code']