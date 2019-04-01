PROJECT TITLE
    This is an simulation of a real-life ATM machine.

BANK MANGER
    Bank manager has default username "admin" and default password "CS207fun"

USER CREATION
    Users are created with a given generated password, which will be printed upon creation inside manager menu.
    Password contains only upper-case, lower-case and numbers with a length between 12 and 24 (inclusive)
    Username must have a length between 5 and 15 (inclusive)

TO EXIT PROGRAM
    You need to keep pressing X until the program closes. Force terminated running instances will not be serialized.

FILES
    Program related files are stored in phase1/data.
    - accReq.txt is for account request. It SHOULD be program generated with format (username accountType isPrimary)
    - alert.txt stores alert messages from atm machine when stock level is low. It can be seen in manager menu.
    - BankData.txt stores BankManager's serializable file (only when program exits normally).
    - deposit.txt stores deposit information. For cheque deposits, follow format (CHEQUE ***) where *** is your deposit
      amount. For cash deposits, follow format (CASH T1 A1 T2 A2 ...) where T1,T2 are cash types, and A1,A2 are amount
      of each cash type. In this phase, we ONLY allow cash types of 5, 10, 20 and 50.
    - outgoing.txt stores billing transaction information. All billing will be recorded in this file.
    - payee.txt stores default billing accounts (payees) with their names. Default are TD_CANADA_TRUST and
      UNIVERSITY_OF_TORONTO
    - testData.txt is used ONLY for TESTING purposes

PROGRAM START
    The main class is /src/ui/MainFrame.java. You need to set initial time when there is no serialized manager found.
    The program shuts down every day between 23:59 and 00:01. Between these times, you are unable to perform any
    actions and all windows will be force-disposed. The program will restart to login menu after the period ends.


There are sufficient guides when errors are thrown. Follow them and you should be okay!

