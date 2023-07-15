import re
import datetime

from interval import Interval
from jqdatasdk import get_trade_days, auth

# 交易所代码字典(根据合约名找到交易所代码)
exchange_dic = {'SHFE': ['RB', 'HC', 'FU', 'BU', 'RU', 'SP', 'CU', 'AL', 'PB', 'ZN', 'NI', 'SN', 'SS', 'AU''AG', 'WR'],
                # 上海期货交易所
                'DCE': ['I', 'J', 'JM', 'A', 'B', 'M', 'Y', 'L', 'V', 'PP', 'EG', 'LH', 'C', 'RR', 'EB', 'CS', 'P',
                        'PG', 'JD', 'BB', 'FB'],  # 大连商品交易所
                'INE': ['SC', 'NR', 'LU', 'BC'],  # 上海能源中心
                'CZCE': ['ZC', 'FG', 'MA', 'RM', 'OI', 'CF', 'SR', 'SA', 'SM', 'CY', 'CJ', 'PTA', 'SF', 'PF', 'AP',
                         'UR', 'LR', 'WH', 'PM', 'RS', 'RI', 'JR'],  # 郑州商品交易所
                'CFFEX': ['IF', 'IH', 'IC', 'TS', 'TF', 'T']  # 中国金融期货交易所
                }

"""
通过合约代码找到合约下单的格式
"""


def get_order_code(code):
    product_code = re.match('\D*', code).group()
    if product_code in exchange_dic['SHFE']:
        exchange = 'SHFE'
        code = code.lower()
    elif product_code in exchange_dic['DCE']:
        exchange = 'DCE'
        code = code.lower()
    elif product_code in exchange_dic['INE']:
        exchange = 'INE'
        code = code.lower()
    elif product_code in exchange_dic['CZCE']:
        exchange = 'CZCE'
        code = re.match('\D*',code).group()+code[-3:]
    else:
        exchange = 'CFFEX'
    # # order_code格式：交易所代码.合约代码
    order_code = exchange + '.' + code
    return order_code




def is_trade_time(dtime):
    """
    dtime:str
    判断dtime是否为交易时间
    只有有任何一个合约处在可交易状态，则此时间就为交易时间
    比如9点只有商品期货可以交易，金融期货还不行，但9点算交易时间
    比如13点商品期货还在休息，但金融期货已经可以交易了，13点也算交易时间
    首先通过交易日期确定是否交易日，是交易日后在判断当前是否为交易时间。
    """
    auth("18974988801", "Bigdata12345678")

    year_m_d = dtime[0:10]  # 判断是否为交易日
    hour_m_s = dtime[-8:]  # 判断是否为开盘时间
    if len(get_trade_days(start_date=dtime, end_date=dtime)):
        trade_time = Interval(hour_m_s, hour_m_s)
        limit_time0 = Interval("09:00:00", "11:30:00")
        limit_time1 = Interval("13:00:00", "15:00:00")
        limit_time2 = Interval("21:00:00", "23:59:00")
        return trade_time in limit_time0 or trade_time in limit_time1 or trade_time in limit_time2
    else:
        # 如果时间为休息日，且上一个日期为交易日，则有夜盘时间:
        myday = datetime.datetime(int(year_m_d[0:4]), int(year_m_d[5:7]), int(year_m_d[8:10])) + datetime.timedelta(
            days=-1)
        yes_time = myday.strftime('%Y-%m-%d')
        if len(get_trade_days(start_date=yes_time, end_date=yes_time)):
            trade_time = Interval(hour_m_s, hour_m_s)
            limit_time0 = Interval("00:00:00", "02:30:00")
            return trade_time in limit_time0
        else:
            return False


if __name__ == '__main__':
    code = 'RB2210'
    print(code)