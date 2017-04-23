# mastodon4j-sample
mastodon4jの実行に必要なaccess_tokenが記載されたプロパティファイルを生成します。
認証APIの呼び出しにmastodon4jを利用しています。

## HOW TO USE
user.propertiesに値を設定し、PropertiesGeneratorを実行してください

## プロパティファイルの解説
* mastodon4j.uri: トークンを利用するMastodonインスタンスのURI (例: https://mstdn.jp)
* mastodon4j.emailAddress: トークンを利用するアカウントのログイン用メールアドレス。
* mastodon4j.password: トークンを利用するアカウントのログイン用パスワード。
* mastodon4j.clientName: 生成したトークンを利用するクライアントアプリケーションの名前。
* mastodon4j.redirectUris: （おそらく）トークン生成後にリダイレクトされるURI。通常は変更しないでください。
* mastodon4j.scopes: トークンを利用したアプリケーションに付与される権限
* mastodon4j.website: あなたのWebサイトのURL (任意)
* mastodon4j.outputPath: プロパティファイルを出力するパス。

##注意
認証情報は大切に扱いましょう。
