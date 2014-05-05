run() {
                        echo "\$ ${@}"
                        "${@}"
                        res=$?
                        if [ $res != 0 ]; then
                          echo
                          echo "Failed!"
                          echo
                          exit $res
                        fi
                      }

                      run tar cf hadoop-2.2.0.tar hadoop-2.2.0
                      run gzip -f hadoop-2.2.0.tar
                      echo
                      echo "Hadoop dist tar available at: /home/zyfly/hadoop/hadoop-2.2.0-src/hadoop-dist/target/hadoop-2.2.0.tar.gz"
                      echo