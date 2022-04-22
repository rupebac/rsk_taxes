1) create a file in ~/.gradle/gradle.properties with something like:

com.rsk.apiKey=<your covalenthq api key>
com.rsk.wallet=<your rsk wallet address>

The api key should look like: ckey_XXXXXXXXXXXXXXX, get it by registering yourself in https://www.covalenthq.com
the second one your wallet address starting with 0x

2) Download the internal transactions of your wallet from RSK explorer. When the popup from RSK explore comes up, select
   as Json, and Field listed All. You will have to adapt the json a bit so that it is a valid container format. Insert
   a '[' at the beginning of the text file, and a ']' at the end of it. Insert a ',' at the end of every line, except in
   the last one. Save this file in src/main/resources/transactions/<wallet_addr>-internal.json You can look at some
   examples that I have left there already, to see how it should look like.


3) go in the cmd line to the directory where you cloned the repo and run "./gradlew :run", or if you are in Windows
   simply "gradlew :run"

   note: if you are developing locally I suggest you run instead: ./gradlew run --args="<wallet_addr> --classpath" so
   that you are not querying covalenthq API every time. Instead, the files from src/main/resources/transactions/<
   wallet_addr>-<page_number>.json will be used, where <wallet_addr> is your RSk wallet address, and <page-number> is an
   incremental integer. You can go in your browser to:
   https://api.covalenthq.com/v1/30/address/<your_wallet_addr>>/transactions_v2/?key=ckey_docs&page-number=0
   and store the output in such files. Increment the last argument number, until in the response you get
   data.pagination.has-more == false

4) you will have the output in a new file "ctc_report.csv". This file is compatible with cryptotaxcalculator.io. If you
   want to customize it for other taxes proogram, you should customize CTCReportGenerator accordingly (feel free to
   create a new pull request if it is a known one).

5) Check in the console output, that you do not have any "UnknownTransactionReport". If you do, check them out manually,
   and let me know a transaction id, to automatize it here.


