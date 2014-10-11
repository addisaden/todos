# todos

todos is a samll command-line app which helps you to
manage your todolist

## Compile

- Leiningen and Git is required

    ```shell
    $ git clone https://github.com/addisaden/todos.git
    $ cd todos
    $ lein uberjar
    ```

The new Jar-File is now in ./target as standalone

## Installation

Download from http://uploads.23g.eu/clojure/todos-0.1.3-standalone.jar

## Usage

Start the app

    $ java -jar todos-0.1.3-standalone.jar

and type help to get started.

    +++++  +++  ++++   +++   ++++
      +   +   +  +  + +   + +
      +   +   +  +  + +   +  +++
      +   +   +  +  + +   +     +
      +    +++  ++++   +++  ++++

    todos
    >> help

    c r e a t e

    create <str>   - create a new todo on the current node
    note <str>     - create a note for current todolist

    e x i t

    exit           - quit the app

    h e l p

    help           - prints a detailed help

    m a n i p u l a t e

    done [<str>]   - set todo (str or current) to done
    rename <str>   - rename the current todolist
    undone [<str>] - reverse of done

    n a v i g a t i o n

    cd ..          - navigate to the parent todo
    cd <str>       - navigate to todo with given name
    first          - navigate to the first todolist
    last           - navigate to the last todolist
    nth <n>        - navigate to todo with position of n (1..)

    r e m e m b e r

    go <str>       - go to the remembered position
    rem <str>      - remember the current navigation
    rem-ls         - list the remembered todos

    r e m o v e

    remove <str>   - remove todolist with the name of str
    remove!        - remove current todolist if its not root
    rm-note <str>  - remove the note with the content of str

    s a v e

    save           - save the complete todolist

    s h o w

    ls             - list the todos (on current navigation)
    ls -a          - list the todos recursively
    ls -l          - list the todos with id
    plain          - shows the raw data


## License

Copyright © 2013 Adrian Enns

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
