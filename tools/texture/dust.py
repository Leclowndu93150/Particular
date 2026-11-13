import sys
from pathlib import Path

import numpy as np
from PIL import Image

ROOT = Path(__file__).resolve().parents[2]
VERSIONS = ['1.20.1', '1.21.1', '26.1.2', '26.2', '26.3']
PARTICLE_DIR = 'common/src/main/resources/assets/particular/textures/particle'

DUST = [(255, 255, 255), (238, 238, 236), (221, 221, 217)]

SIZE = 12
FRAMES = [
    [
        '............',
        '............',
        '............',
        '............',
        '............',
        '....0001....',
        '...000011...',
        '..00001112..',
        '..0001122...',
        '............',
        '............',
        '............',
    ],
    [
        '............',
        '............',
        '............',
        '....000.....',
        '...0000001..',
        '..000000111.',
        '.0000001112.',
        '.0000111122.',
        '..00111222..',
        '............',
        '............',
        '............',
    ],
    [
        '............',
        '............',
        '...00...00..',
        '..0000.0001.',
        '.0000000011.',
        '.0000001112.',
        '.0000111122.',
        '..00111222..',
        '...11222....',
        '............',
        '............',
        '............',
    ],
    [
        '............',
        '..00........',
        '.0001...00..',
        '.0011..0001.',
        '..110..0012.',
        '.....00.112.',
        '..00.011....',
        '.0011.12....',
        '..122.......',
        '............',
        '............',
        '............',
    ],
    [
        '............',
        '............',
        '..00....00..',
        '.001....012.',
        '..1......2..',
        '............',
        '....00......',
        '...0011.....',
        '....12......',
        '............',
        '............',
        '............',
    ],
]


def sprite(rows):
    out = np.zeros((SIZE, SIZE, 4), np.uint8)
    for y, row in enumerate(rows):
        for x, key in enumerate(row):
            if key != '.':
                out[y, x, :3] = DUST[int(key)]
                out[y, x, 3] = 255
    return out


def build():
    return {f'dust_puff_{i}.png': sprite(rows) for i, rows in enumerate(FRAMES)}


def main():
    files = build()
    targets = [Path(sys.argv[1])] if len(sys.argv) > 1 else [ROOT / 'versions' / v / PARTICLE_DIR for v in VERSIONS]
    for target in targets:
        target.mkdir(parents=True, exist_ok=True)
        for name, img in files.items():
            Image.fromarray(img, 'RGBA').save(target / name)


if __name__ == '__main__':
    main()
