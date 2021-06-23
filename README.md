
## 初期設定
### 前提条件
 - docker
 - docker-compose

### 初回作業
- docker で postgresql を起動
~~~
cd docker
docker-compose up -d
~~~
- CmsApplication起動 -> テーブル自動作成
- 初期データ登録: src/main/resources/database/create-initial-user.sql 内のDMLを実行


### docker操作
- 起動: docker-compose up -d
- 停止: docker-compose stop
- ボリューム削除(データ消える): docker-compose down -v
- 状態確認: docker-compose ps
