# FindColorCode 
## 概要
FindColorCodeは、ユーザーがオリジナルカラーを作成し、そのカラーコード（16進数）を保存・管理するAndroidアプリです。RGBシークバーを使用して色を調整し、カラーコードを取得、色のメモをつけて保存できます。

## 機能
- RGBシークバーを使って自由に色を調整
- カラーコードを入力して色を表示
- 作成した色に名前とメモをつけて保存
- 保存した色をフィルタリングして検索
- 色のプレビューを2つ表示して組み合わせを確認

## 使用技術
- **開発言語**: Kotlin
- **UIフレームワーク**: Jetpack Compose
- **アーキテクチャ**: MVVM
- **データベース**: Room
- **API**: The Color API（カラーパレット取得）
- **その他ライブラリ**:
  Retrofit
  Moshi
  Coroutine
  LiveData
  Room
  Navigation Compose
  Material3
  Junit

  ・SDKVersion
  targetSdkVersion:34
  compileSdkVersion:34
  minSdkVersion:23

### 必要なもの：
- Android Studio
- JDK 8以降

### 手順：
1. リポジトリをクローンします：
    git clone <https://github.com/kisayama/Find-ColorCode.git>
2. プロジェクトをAndroid Studioで開きます。
3. 必要な依存関係を同期します。
4. アプリをビルドし、実機またはエミュレータで実行します。

## 使用方法

1. RGBシークバーやシークバー横のテキストフィールドを使って色を調整します。
   (カラーコードが表示されてるテキストフィールドに色名やHexを入力してもOK)
2.テキストフィールド横の＋ボタンからメモと名前をつけて保存します。
4. 保存した色を一覧から検索・編集できます。
5. 基本の色タブから色を選択することもできます
6. カラーパレットを作るタブから現在選択している色のカラーパレットを作成することができます

