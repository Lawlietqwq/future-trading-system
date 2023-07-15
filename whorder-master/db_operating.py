from db_utils import db_connect

"""
通过模型id得到模型实例
"""
def get_trading_holding(model_id):
    db,cursor = db_connect()
    sql = f"select `uid`,`code`,`open_price`,`bk_or_sk`,`open_num`,`open_time` " \
          f"from jq_futures.`trading_holding` where model_id = '{model_id}'"
    cursor.execute(sql)
    record = cursor.fetchone()
    db.close()
    return record




"""
插入持仓记录
return : 是否插入成功以及holding_id
"""


def insert_holding_record(uid, model_id, code, buy_or_sell, open_num, open_price, open_time):
    success = True
    db, cursor = db_connect()
    columns = ['uid', 'model_id', 'code', 'open_price', 'bk_or_sk', 'open_num']
    sql = f"insert into jq_futures.trading_holding ({','.join(['`' + col + '`' for col in columns])},`open_time`) " \
          f"values ('{uid}','{model_id}', '{code}','{open_price}', '{buy_or_sell}','{open_num}','{open_time}') "
    try:
        cursor.execute(sql)
        db.commit()
    except Exception:
        db.rollback()  # 如果错误，回滚操作
        success = False
    finally:
        db.close()

    return success


"""
插入交易记录
arg：
"""


def insert_trading_history(uid, model_id, code, buy_or_sell, lot,
                           open_price, open_time,
                           close_price, close_time,
                           profit):
    success = True
    db, cursor = db_connect()
    columns = ['uid', 'model_id', 'code', 'bk_or_sk', 'lot', 'open_price', 'open_time', 'close_price',
               'close_time', 'profit']
    sql = f"insert into jq_futures.trading_history ({','.join(['`' + col + '`' for col in columns])}) " \
          f"values ('{uid}','{model_id}', '{code}','{buy_or_sell}','{lot}','{open_price}'," \
          f" '{open_time}','{close_price}','{close_time}','{profit}') "

    try:
        cursor.execute(sql)
        db.commit()
    except Exception:
        success = False
        db.rollback()
    finally:
        db.close()

    return success


"""
删除持仓记录
args: holding_id:持仓记录的id
return 是否删除成功
"""


def delete_holding_records(model_id):
    success = True
    db, cursor = db_connect()
    sql = f"delete from jq_futures.trading_holding where model_id='{model_id}'"
    try:
        cursor.execute(sql)
        db.commit()
    except Exception:
        db.rollback()
        success = False
    finally:
        db.close()
    return success


"""
修改持仓记录的持仓数
args：holding_id：持仓记录的id
      remain_num: 该持仓记录的剩余持仓
return : 是否修改成功
"""


def update_holding_records(model_id, remain_num):
    success = True
    db, cursor = db_connect()
    sql = f"update jq_futures.trading_holding set `open_num`='{remain_num}' where `model_id`={model_id}"
    try:
        cursor.execute(sql)
        db.commit()
    except Exception:
        db.rollback()
        code = False
    finally:
        db.close()
    return success


def insert_available_futures_company():
    available_futures_company = ['simnow',
                                 'A安粮期货',
                                 'B渤海期货', 'B倍特期货', 'B宝城期货',
                                 'C长江期货', 'C长城期货', 'C创元期货',
                                 'D东方汇金', 'D大地期货', 'D大陆期货', 'D东华期货', 'D大越期货', 'D大有期货',
                                 'F福能期货', 'F方正中期',
                                 'G国泰君安', 'G冠通期货', 'G广发期货', 'G广金期货', 'G国际期货', 'G国元期货', 'G国金期货',
                                 'G国贸期货', 'G国信期货', 'G国投安信', 'G格林大华', 'G国盛期货', 'G国富期货', 'G国海良时',
                                 'H华龙期货', 'H红塔期货', 'H华鑫期货', 'H海航期货', 'H恒泰期货', 'H宏源期货', 'H华安期货',
                                 'H华金期货', 'H海证期货', 'H和合期货', 'H和融期货', 'H海通期货', 'H华闻期货', 'H徽商期货', 'H混沌天成',
                                 'J金石期货', 'J建信期货', 'J金信期货', 'J金瑞期货', 'J锦泰期货', 'J江海汇鑫', 'J金元期货',
                                 'M民生期货', 'M迈科期货',
                                 'N南华期货',
                                 'Q齐盛期货',
                                 'R瑞奇期货', 'R瑞达期货',
                                 'S山西三立', 'S神华期货', 'S盛达期货', 'S山金期货', 'S上海东亚', 'S上海东方', 'S上海中期',
                                 'T通惠期货', 'T铜冠金源',
                                 'W五矿期货',
                                 'X鑫鼎盛期货', 'X先锋期货', 'X西南期货', 'X先融期货', 'X新纪元期货', 'X新湖期货', 'X西部期货', 'X兴证期货', 'X新世纪期货',
                                 'Y英大期货', 'Y一德期货',
                                 'Z中信期货', 'Z中财期货', 'Z中钢期货', 'Z中天期货', 'Z中原期货', 'Z中航期货', 'Z中辉期货', 'Z中银期货',
                                 'Z招商期货', 'Z中州期货', 'Z中衍期货', 'Z中融汇信', 'Z浙商期货', 'Z中粮期货', 'Z紫金天风', 'Z中泰期货', ]
    db, cursor = db_connect()
    for company in available_futures_company:
        sql = f"insert into jq_futures.company (`company_name`) values ('{company}')"
        cursor.execute(sql)
    db.commit()
    db.close()


if __name__ == '__main__':
    print(get_model_instance(72))
    # insert_available_futures_company()
