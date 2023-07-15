import datetime
import re
import time

from db_operating import delete_holding_records, update_holding_records, insert_trading_history, insert_holding_record, \
    get_trading_holding
from db_utils import db_connect
from tqsdk import TqApi, TqAuth, TqKq, TqAccount

from util import get_order_code, is_trade_time

"""
获取账户资金
"""


def get_account_funds(xinyi_account, xinyi_pwd, futures_company, trading_account, trading_pwd):
    if trading_account == '' and trading_pwd == '':  # 模拟交易
        api = TqApi(TqKq(), auth=TqAuth(xinyi_account, xinyi_pwd))

    else:  # 实盘交易
        api = TqApi(TqAccount(futures_company, trading_account, trading_pwd),
                    auth=TqAuth(xinyi_account, xinyi_pwd))
    account = api.get_account()
    return account


# """
# 获取某个合约的持仓情况
# """
#
# def get_code_position():
#     position = get_position(self.order_code)
#     return position

"""
下单
"""


def open(api, uid, model_id, code, buy_or_sell, open_num, price: float = None):
    direction = 'BUY' if buy_or_sell == 0 else 'SELL'

    order_code = get_order_code(code)

    # 获得某个合约的行情引用
    quote = api.get_quote(order_code)

    # 下单并返回委托单的引用，当该委托单有变化时 order 中的字段会对应更新
    if price is None:
        try:
            api.wait_update(deadline=time.time() + 10)
        except Exception as e:
            print(e)
        if buy_or_sell == 0:
            price = quote.ask_price1
        else:
            price = quote.bid_price1
    else:
        price = float(price)

    order = api.insert_order(symbol=order_code, direction=direction, offset='OPEN', volume=open_num,
                             limit_price=price)

    deadline_time = datetime.datetime.now() + datetime.timedelta(seconds=1)

    while True:
        api.wait_update()
        if api.is_changing(order, ['status']):
            print("单状态: %s, 已成交: %d 手" % (order.status, order.volume_orign - order.volume_left))
        # if self.api.is_changing(order,["status"]):
        #     break
        if order.status == 'FINISHED':
            break
        if datetime.datetime.now() > deadline_time:
            api.cancel_order(order)
            api.wait_update()
            return {'code': 300, 'msg': '响应时间过长,撤单'}

    result = {'code': 200, 'msg': '开仓成功'}

    # trade_price:平均成交价,
    if not order.is_error:
        open_time = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")

        success = insert_holding_record(uid, model_id, code, buy_or_sell, open_num, order.trade_price, open_time)
        if not success:
            result['code'] = 500
            result['msg'] = '插入持仓记录失败'
            return result
    else:
        result['code'] = 500
        result['msg'] = '开仓失败'

    return result


def close(api, model_id, lot, price):
    record = get_trading_holding(model_id)

    # 开仓是买入的话平仓就是卖出
    direction = 'BUY' if record['bk_or_sk'] == 1 else 'SELL'

    order_code = get_order_code(record['code'])
    quote = api.get_quote(order_code)

    # 下单并返回委托单的引用，当该委托单有变化时 order 中的字段会对应更新
    if price is None:
        api.wait_update(deadline=time.time() + 10)
        if record['bk_or_sk'] == 1:
            price = quote.ask_price1
        else:
            price = quote.bid_price1
    else:
        price = float(price)

    order = api.insert_order(symbol=order_code, direction=direction, offset='CLOSETODAY', volume=lot,
                             limit_price=price)

    deadline_time = datetime.datetime.now() + datetime.timedelta(seconds=30)

    while True:
        api.wait_update()
        if order.is_error:
            order = api.insert_order(symbol=order_code, direction=direction, offset='CLOSE',
                                     volume=lot,
                                     limit_price=price)
        if datetime.datetime.now() > deadline_time:
            api.cancel_order(order)
            api.wait_update()
            return {'code': 300, 'msg': '响应时间过长,撤单'}
        print("单状态: %s, 已成交: %d 手" % (order.status, order.volume_orign - order.volume_left))
        if order.status == 'FINISHED':
            break

    result = {'code': 200, 'msg': '平仓成功'}
    # trade_price:平均成交价,
    if not order.is_error:
        close_time = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        profit = (order.trade_price - record['open_price']) * lot

        # 插入交易记录
        success = insert_trading_history(record['uid'], model_id, record['code'], record['bk_or_sk'], lot,
                                         record['open_price'], record['open_time'],
                                         order.trade_price, close_time, profit)

        if not success:
            return {'code': 500, 'msg': '插入交易记录失败'}

        remain_num = record['open_num'] - lot

        if remain_num == 0:
            success = delete_holding_records(model_id)
            if success is False:
                result['msg'] = '删除持仓失败'
                result['code'] = 500
                return result
            result['code'] = 200
            result['msg'] = '平仓成功'
        else:
            success = update_holding_records(model_id, remain_num)
            if success is False:
                result['code'] = 500
                result['msg'] = '修改持仓信息失败'
            result['code'] = 300
    else:
        result['code'] = 500
        result['msg'] = '平仓失败'

    return result


if __name__ == "__main__":
    # # code = 'H2210'
    # api = ClientAPI('RB2210', "13212630913", "123456789")
    # print(api.order("SHFE.rb2210", "0", "1", 1))
    # data = {'a': 1, 'b': 2}
    # data.get('a')
    # data.get('c')
    now = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    print(now)
    now_date = datetime.datetime.today().date()
    print(now_date)
    print(is_trade_time("2022-07-28 12:02:40"))
