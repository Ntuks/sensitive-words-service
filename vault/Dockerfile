FROM ubuntu:20.04
LABEL authors="ntuthko"

RUN apt-get update && apt-get install -y software-properties-common curl gnupg2 && \
  curl -fsSL https://apt.releases.hashicorp.com/gpg | apt-key add - && \
  apt-add-repository "deb [arch=amd64] https://apt.releases.hashicorp.com $(lsb_release -cs) main" && \
  apt-get update && apt-get install -y \
  vault && \
  setcap cap_ipc_lock= /usr/bin/vault

COPY /vaul-init.sh  /app/vault-init.sh

RUN chmod +x /app/vault-init.sh

CMD ["/app/vault-init.sh"]