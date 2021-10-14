FROM gradle:7.2.0-jdk11
COPY . .
RUN gradle assemble --no-daemon
CMD ["gradle","run"]
